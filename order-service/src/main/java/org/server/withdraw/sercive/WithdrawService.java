package org.server.withdraw.sercive;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import jodd.util.StringUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.server.dao.BankCodeDAO;
import org.server.dao.OrderDAO;
import org.server.dao.WalletsDAO;
import org.server.dao.WithdrawChannelDao;
import org.server.mapper.BankCodeMapper;
import org.server.mapper.OrderMapper;
import org.server.mapper.WithdrawBankChannelMapper;
import org.server.mapper.WithdrawOrderMapper;
import org.server.service.OrderIdService;
import org.server.service.WalletService;
import org.server.withdraw.dto.TraderResponseDto;
import org.server.withdraw.model.TraderResponseCode;
import org.server.withdraw.model.WithdrawOrder;
import org.server.withdraw.req.CreateWithdrawRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


@Log4j2
@Service
public class WithdrawService {

  @Resource
  private BankCodeMapper bankCodeMapper;
  @Resource
  private OrderMapper orderMapper;

  @Resource
  private WithdrawBankChannelMapper withdrawBankChannelMapper;

  @Resource
  private WalletService walletService;

  @Resource
  private WithdrawOrderMapper withdrawOrderMapper;

  @Resource
  private RedisTemplate<String, String> redisTemplate;

  @Resource
  private OrderIdService orderIdService;



  //二次驗籤 (第三方API)
  @Value("${withdraw.order.verify.secondary.enable:false}")
  private Boolean withdrawOrderVerifyEnable;

  //配置同一張卡在某段最大請求數的 (時間 分)限制
  @Value("${withdraw.order.continuous.minute:10}")
  private Integer withdrawContinuousMinute;

  //同一張卡在某段時間的最大請求的 (數)限制
  @Value("${withdraw.order.continuous.count:3}")
  private Integer withdrawContinuousCount;

  //限制同一卡號3分鐘內只能發一單
  @Value("${withdraw.order.verify.minute.enable:true}")
  private Boolean withdrawOrderVerifyMinuteEnable;

  private final String REDIS_KEY_PREFIX = "withdrawOrder:PayeeCardNo:";

  private TraderResponseDto createWithdraw(CreateWithdrawRequest request, String ip)
      throws NoSuchFieldException, IllegalAccessException {
    String logPrefix = "【用戶提現下單】";
    log.info("{}createWithdraw: {}", logPrefix, request);

    // 商戶驗證(驗商戶代碼/驗簽)
    BankCodeDAO dao = verifyRequest(request);

    TraderResponseDto dto;
    try {

      if (dao.getStatus() == BankCodeDAO.STATUS_DISABLED) {
        log.info("{} merchant=[{}],merchant status is DISABLED", logPrefix,request.getUserId());
        //TODO 拋異常 停用
//        throw new XxPayTraderException(TraderResponseCode.MERCHANT_DISABLED);
      }

      if(StringUtil.isBlank(request.getBankName())){
        log.info("{} merchant=[{}],bankName is:{} ", logPrefix, request.getUserId() , request.getBankName());
        //TODO  拋異常 找不到銀行
//        throw new XxPayTraderException(TraderResponseCode.WITHDRAW_CHANNEL_BANK_NAME_IS_REQUIRED);
      }

      // 檢查訂單是否存在
      checkIfUserOrderNoAlreadyExists(request.getUserId(), request.getOrderNo());
      TraderResponseCode responseCode = checkIfAmountIsSupported(request.getUserId(), request.getAmount());


      //初始 status 生成中
      int status = WithdrawOrder.STATUS_COMPLETED_ING;
      String remark = null;

      //TraderResponseCode != SUCCESS
      if(!responseCode.getCode().equals(TraderResponseCode.SUCCESS.getCode())){
        status = WithdrawOrder.STATUS_FAILED_ON_PROCESSING;
        remark = responseCode.getMessage();

        // SUCCESS 後是否二次驗籤
      } else {
        //是否在二次驗籤 (二次驗證接口) 暫時無
        if (withdrawOrderVerifyEnable) {
          //使用 orderNo 來驗證簽名
//          WithdrawVerifyOrderDto verifyOrderDto = WithdrawVerifyOrderDto.builder()
//              .orderNo(request.getOrderNo())
//              .build();
//          verifyOrderDto.sign(dao.getRequestKey());
//          String merchantUrl = withdrawOrderVerifyUrl.replace("{merchant}", "50"+request.getClientExtra().trim().substring(1, request.getMerchantId().length()));
//          log.info("createWithdraw merchantUrl: {}", merchantUrl);
//          log.info("createWithdraw verifyUrl: {}", verifyUrl);
//          HttpResponse response = FormDataUtil.get(verifyUrl);
//          org.apache.http.HttpEntity entity = response.getEntity();
//          String resStr = EntityUtils.toString(entity, "utf-8");
//          log.info("createWithdraw resStr: {}", resStr);
//          if (StringUtils.isNotBlank(resStr) && resStr.equals("TRUE")) {
//            status = WithdrawOrder.STATUS_PENDING;
//          } else {
//            status = WithdrawOrder.STATUS_FAILED_ON_PROCESSING;
//            remark = TraderResponseCode.WITHDRAW_ORDER_VERIFICATION_FAILED.getMessage();
//            responseCode = TraderResponseCode.WITHDRAW_ORDER_VERIFICATION_FAILED;
//            log.error("平台 merchantOrderNo: {}, msg: {}", request.getMerchantOrderNo(), responseCode.getMessage());
//          }
//        }
        }
      }

      if (status == WithdrawOrder.STATUS_COMPLETED_ING) {
        WithdrawOrder query = new WithdrawOrder();
        query.setAccountId(request.getAccountId());
        query.setPayeeCardNo(request.getPayeeCardNo());
        query.setWithdrawMinute(withdrawContinuousMinute);
        //判斷一定時間(分鐘)內是否有 重複投單
        Integer verifyCount = withdrawOrderMapper.findVerifyPayeeCardNoCount(query);
        log.info("createWithdraw merchantId: {}, payeeCardNo: {}, verifyCount: {}", query.getUserId(), query.getPayeeCardNo(), verifyCount);
        if (verifyCount >= withdrawContinuousCount) {
          status = WithdrawOrder.STATUS_FAILED_ON_PROCESSING;
          remark = TraderResponseCode.WITHDRAW_METHOD_PAYEE_CARD_NO_BANK_QUANTITY_CONTINUOUS.getMessage();
          responseCode = TraderResponseCode.WITHDRAW_METHOD_PAYEE_CARD_NO_BANK_QUANTITY_CONTINUOUS;
        }
      }

//      限制同一卡號3分鐘內只能發一單
      log.info("限制同一卡號3分鐘內只能發一單檢查開啟:{}", withdrawOrderVerifyMinuteEnable);
      if(withdrawOrderVerifyMinuteEnable){
        String redisKey = REDIS_KEY_PREFIX + request.getPayeeCardNo();
        String payeeCardNo = redisTemplate.opsForValue().get(redisKey);
        //Redis 檢查key
        if(StringUtils.isNotBlank(payeeCardNo)){
          log.info("{} merchant=[{}],PayeeCardNo is:{}, please try again after 3 min.", logPrefix, request.getUserId(), payeeCardNo);
          status = WithdrawOrder.STATUS_FAILED_ON_PROCESSING;
          remark = TraderResponseCode.WITHDRAW_METHOD_PAYEE_CARD_NO_BANK_CONTINUOUS.getMessage();
          responseCode = TraderResponseCode.WITHDRAW_METHOD_PAYEE_CARD_NO_BANK_CONTINUOUS;
        }else{
          if (status == WithdrawOrder.STATUS_COMPLETED_ING) {
            redisTemplate.opsForValue().set(redisKey, request.getPayeeCardNo());
            redisTemplate.expire(redisKey, 3, TimeUnit.MINUTES);
            log.info("{} set redis key:{} 3min", logPrefix, redisKey);
          }
        }
      }
      WithdrawOrder withdrawOrder = WithdrawOrder.builder()
          .withdrawOrderId(orderIdService.getWithdrawId())
          .userId(request.getUserId())
          .bankOrderNo(request.getOrderNo())
          .amount(request.getAmount())
          .actualAmount(BigDecimal.ZERO)
          .rateFixedAmount(BigDecimal.ZERO)
          .rate(0.0)
          .currency(request.getCurrency())
          .clientIp(request.getClientIp())
          .clientDevice(request.getClientDevice())
          .clientExtra(request.getClientExtra())
          .notifyUrl(request.getNotifyUrl())
          .payeeCardNo(request.getPayeeCardNo())
          .payeeCardName(request.getPayeeCardName())
          .bankName(request.getBankName())
          .branchName(request.getBranchName())
          .bankProvince(request.getBankProvince())
          .bankCity(request.getBankCity())
          .status(status)// 先收單待Job分配渠道
          .remark(remark)
          .build();

      // 建立支付中订单
      int result = withdrawOrderMapper.insertWithdrawOrder(withdrawOrder);
      log.info("{}【創建withdraw Order】===> merchantId:{}, withdrawOrder:{}, insert result:{}", logPrefix, request.getUserId(), withdrawOrder, result);
      dto = TraderResponseDto.success();
      //回覆mpa.put withdrawOrderId
      dto.put("withdrawOrderId", withdrawOrder.getWithdrawOrderId());
      log.info("{} WithdrawOrderId=[{}] is created status:{}", logPrefix, withdrawOrder.getWithdrawOrderId(), status);

      //處理 失敗的返回
      if(!responseCode.getCode().equals(TraderResponseCode.SUCCESS.getCode())){
        dto = new TraderResponseDto();
        dto.setCode(responseCode);
      }
    }catch (Exception e){
      log.error("{} create exception:{}", logPrefix, e.getMessage(), e);

      dto = new TraderResponseDto();
      dto.setCode(TraderResponseCode.UNKNOWN_ERROR, e.getMessage());
      dto.sign(dao.getRequestKey());
    }
    dto.sign(dao.getRequestKey());
    return dto;
  }

  private BankCodeDAO verifyRequest(CreateWithdrawRequest request)
      throws  NoSuchFieldException, IllegalAccessException {

    BankCodeDAO dao = bankCodeMapper.selectById(request.getUserId());

    //Merchant對照
//    Merchan
    if (dao == null) {
      //TODO 拋異常 沒有該銀行
//      throw new XxPayTraderException(TraderResponseCode.MERCHANT_NOT_FOUND);
    }

    //暫時無
//    request.setVerifyCode(withdrawOrderVerifyCode);

    //驗證傳來的 簽名(對方已將物件屬性簽名) 核對 我們db裡存的 requestKey(依照物件屬性重簽)
    if (!request.verify(dao.getRequestKey())) {
      log.info("本地 OrderNo: {}, msg: {}", request.getOrderNo(), TraderResponseCode.SIGN_VERIFICATION_FAILED.getMessage());
      //TODO 拋異常 驗籤失敗
      //      throw new XxPayTraderException(TraderResponseCode.SIGN_VERIFICATION_FAILED);
    }
    return dao;
  }

  private void checkIfUserOrderNoAlreadyExists(String userId, String orderNo) {

    //使用orderNo 和 userId 查尋是否有重複的訂單
    List<OrderDAO> orderDAOS = orderMapper.selectByIdOrUserId(orderNo, userId);
    if(orderDAOS != null && !orderDAOS.isEmpty()){
      log.info("{}使用者訂單號重複試: {}", userId, orderNo);
//      拋異常
//      TraderResponseCode.MERCHANT_ORDER_NO_DUPLICATE,
    }
  }


  //查詢是否可出款 包含餘額
  private TraderResponseCode checkIfAmountIsSupported(String userId, BigDecimal amount) {

    WithdrawChannelDao dao = withdrawBankChannelMapper.getWithdrawChannelDaoByUserId(
        userId);
    if(dao != null ){
      //找不到 渠道
      return TraderResponseCode.WITHDRAW_CHANNEL_NOT_FOUND_USABLE;
    }

    if(dao.getBalance().compareTo(BigDecimal.ZERO) <= 0){
      return TraderResponseCode.WITHDRAW_CHANNEL_BALANCE_NOT_ENOUGH;
    }

    if(dao.getBalance().compareTo(amount)  <= 0){
      return TraderResponseCode.WITHDRAW_CHANNEL_BALANCE_NOT_ENOUGH;
    }

    return TraderResponseCode.SUCCESS;
  }

}
