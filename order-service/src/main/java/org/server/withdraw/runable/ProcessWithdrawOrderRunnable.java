package org.server.withdraw.runable;


import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import lombok.extern.log4j.Log4j2;
import org.apache.http.conn.ConnectTimeoutException;
import org.server.mapper.WithdrawChannelBankCodeMapper;
import org.server.withdraw.model.WithdrawChannel;
import org.server.mapper.WithdrawBankChannelMapper;
import org.server.mapper.WithdrawOrderMapper;
import org.server.withdraw.model.WithdrawChannelBankCode;
import org.server.withdraw.model.WithdrawOrder;
import org.server.withdraw.sercive.ExecuteWithdrawService;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Log4j2
public class ProcessWithdrawOrderRunnable implements Runnable {

  @Resource
  private WithdrawOrderMapper withdrawOrderMapper;

  @Resource
  private WithdrawBankChannelMapper withdrawBankChannelMapper;

  @Resource
  private WithdrawChannelBankCodeMapper withdrawChannelBankCodeMapper;

  private WithdrawOrder withdrawOrder;

  public ProcessWithdrawOrderRunnable(WithdrawOrder withdrawOrder) {
    this.withdrawOrder = withdrawOrder;
  }

  @Override
  public void run() {
    //確認是否符合全部要求
    boolean isLegalData = false;

    try {
      log.info("{} WithdrawOrderId:{}, 开始送单", ExecuteWithdrawService.logPrefix, withdrawOrder.getWithdrawOrderId());
      // 檢查代付銀行是否可用
      checkIfBankIsSupported(withdrawOrder.getMerchantId(),withdrawOrder.getBankName());
      // 過濾商戶所設置的銀行卡黑名單
      // 檢查帳號下的渠道可接受此訂單金額,若無則拋exception
      // 检查银行卡号是否合法
      log.info("{} WithdrawOrderId:{}, 检查参数完成", ExecuteWithdrawService.logPrefix, withdrawOrder.getWithdrawOrderId());




      isLegalData = true;


    } catch (Exception e) {
      //以下三種異常 不做處理 (可能是預期的問題)
      if (e instanceof SocketTimeoutException || e instanceof SocketException
          || e instanceof ConnectTimeoutException) {

      } else {
        // 送单失败，回调商户
        log.error("{} WithdrawOrderId:{}, exception:{}", ExecuteWithdrawService.logPrefix,
            withdrawOrder.getWithdrawOrderId(), e.getMessage(), e);
        processWithdrawOrderOnFail(isLegalData, withdrawOrder, e);
      }
    }
  }



  /**
   * 检查银行是否支援
   *
   * @param merchantId	商户号
   * @param bankName		银行名
   */
  private void checkIfBankIsSupported(String merchantId, String bankName) {

    //TODO

    // 依merchant找出所有的代付帳號並檢查是否可用銀行
    List<WithdrawChannel> channelList = withdrawBankChannelMapper.findByMerchantIds(merchantId);

    //ˊ找不到可使用渠道
    if (channelList == null || channelList.size()==0) {
      //TODO  找不到可使用渠道異常
//      throw new XxPayTraderException(TraderResponseCode.WITHDRAW_CHANNEL_NOT_FOUND_USABLE);
    }

    for(WithdrawChannel channel: channelList) {
      List<WithdrawChannelBankCode> bankList = withdrawChannelBankCodeMapper.findByBankName(bankName);

    }

  }




  /**
   * 提款訂單处理失败
   *
   * @param isLegalData	false＝创建失败, true=支付失败
   * @param withdrawOrder	代付订单资讯
   * @param exception		错误
   */
  @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ , rollbackFor = Exception.class)
  public void processWithdrawOrderOnFail(boolean isLegalData, WithdrawOrder withdrawOrder, Exception exception) {

    //不符合要求 創建失敗
    if(!isLegalData){
      withdrawOrder.setStatus(WithdrawOrder.STATUS_FAILED_ON_PROCESSING);
    }
    //符合要求但是  支付失败
    if(isLegalData){
      withdrawOrder.setStatus(WithdrawOrder.STATUS_FAILED_ON_WITHDRAWING);
    }

    withdrawOrder.setRemark(exception.getMessage());
    int i = withdrawOrderMapper.updateWithdrawOrder(withdrawOrder);

    //TODO




  }

}
