package org.server.withdraw.sercive;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import lombok.extern.log4j.Log4j2;
import org.server.mapper.WithdrawOrderMapper;
import org.server.utils.DistributedLock;
import org.server.withdraw.model.WithdrawOrder;
import org.server.withdraw.runable.ProcessWithdrawOrderRunnable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class ExecuteWithdrawService {

  public static final String logPrefix = "【執行訂單分配提交】";

  private final static String LOCK_KEY_PREFIX = "ExecuteWithdrawService.processWithdrawOrder.WithdrawOrderId";

  @Value("${distributed.redis.lock.seconds.timeout:500}")
  private Integer timeout;

  @Resource
  private WithdrawOrderMapper withdrawOrderMapper;

  @Resource
  private DistributedLock distributedLock;

  /**
   *
   * 排程20秒打一次接口，redis對此接口上鎖10分鐘，假設處理一張訂單最多要花15秒(timeout+反查訂單是否存在)
   * 一個線程一分鐘最少可以處理訂單4張訂單，最大開到5個線程
   * 粗估總處理訂單數: 4*5*10=200
   *
   * 保險起見目前先設100，不夠用時再調整
   */
  private final static ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
      3,                            // corePoolSize: 核心線程數，即線程池的基本大小，保持活動狀態的最小線程數。
      5,                                        // maximumPoolSize: 最大線程數，即線程池允許創建的最大線程數。
      60,                                       // keepAliveTime: 線程的空閑時間，超過這個時間，多餘的空閑線程會被回收。
      TimeUnit.SECONDS,                         // unit: keepAliveTime 參數的時間單位，這裏設置為秒。
      new ArrayBlockingQueue<>(100),   // workQueue: 用於保存等待執行任務的阻塞隊列。這裏使用了 ArrayBlockingQueue，可根據需求選擇其他隊列實現。
      new ThreadPoolExecutor.AbortPolicy()      // handler: 拒絕策略，當任務提交數超過 maximumPoolSize 且隊列已滿時，採取的拒絕策略。這裏使用 AbortPolicy，即拋出 RejectedExecutionException。
  );


  //處裡 生成中的訂單
  public void processWithdrawOrder() {

    //取得 訂單生成中 三天內的訂單
    List<WithdrawOrder> withdrawOrderList = withdrawOrderMapper.selectByStatusAndDay(WithdrawOrder.STATUS_COMPLETED_ING,3);

    if (withdrawOrderList == null || withdrawOrderList.size() <= 0) {
      log.info("{} 目前沒有待分配訂單需處理", logPrefix);
      return;
    }

    // *** 避免PROD兩台scheduler同時打接口，會造成重複代付問題做以下處理 ***
    List<WithdrawOrder> pendingWithdrawOrderList = new ArrayList<WithdrawOrder>();
    withdrawOrderList.forEach(withdrawOrder -> {
      String orderId = withdrawOrder.getWithdrawOrderId();
      if(!distributedLock.isLock(LOCK_KEY_PREFIX, orderId)) {
        // 1.此訂單未上鎖，先將此訂單上鎖 key = LOCK_KEY_PREFIX+ orderId
        boolean isLock = distributedLock.lock(timeout, LOCK_KEY_PREFIX, orderId);
        log.info("{} WithdrawOrderId:{}, 上鎖{}秒, 上鎖結果:{}", logPrefix, orderId, timeout, isLock);

        // 2.將訂單狀態改為"提交處理中"
        withdrawOrder.setStatus(WithdrawOrder.STATUS_PROCESSING);
        withdrawOrderMapper.updateWithdrawOrderStatus(withdrawOrder);

        // 3.排入待處理訂單 此時每筆訂單還沒處理完整 還不能解鎖
        pendingWithdrawOrderList.add(withdrawOrder);
      } else {
        log.info("{} WithdrawOrderId:{}發現已上鎖, 可能由另外一台機器處理此訂單", logPrefix, orderId);
      }
    });

    log.info("{} 原待分配渠道訂單數:{}, 經檢查后待處理訂單數:{}", logPrefix, withdrawOrderList.size(), pendingWithdrawOrderList.size());

    pendingWithdrawOrderList.forEach(withdrawOrder -> {
      try {
        //在線程池裡開異步任務
        threadPool.execute(new ProcessWithdrawOrderRunnable(withdrawOrder));
      }catch (RejectedExecutionException e) {
        // RejectedExecutionException 當線程池無法接受新任務時（通常是由於線程池已關閉或達到容量限制）
        // Queue滿了，啟動拒絕策略
        log.info("{} WithdrawOrderId:{}, Queue滿了啟動拒絕策略, 等待下次處理 error:{}", logPrefix, withdrawOrder.getWithdrawOrderId(), e.getMessage());

        // 將訂單狀態改為"訂單生成中"，等待下次處理
        WithdrawOrder checkOrder = withdrawOrderMapper.selectByWithdrawOrderId(withdrawOrder.getWithdrawOrderId());
        if(checkOrder.getStatus().equals(WithdrawOrder.STATUS_PROCESSING)) {
          withdrawOrder.setStatus(WithdrawOrder.STATUS_COMPLETED_ING);
          withdrawOrderMapper.updateWithdrawOrderStatus(withdrawOrder);
          log.info("{} WithdrawOrderId:{}, 將訂單狀態改為\"待分配渠道\"，等待下次處理", logPrefix, withdrawOrder.getWithdrawOrderId());
        }
        // 解鎖 這裏才算每筆訂單結束 解鎖
        distributedLock.unlock(LOCK_KEY_PREFIX, withdrawOrder.getWithdrawOrderId());
      }
    });


  }

}
