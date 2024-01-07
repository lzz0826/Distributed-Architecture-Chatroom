package org.server.withdraw.sercive;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import jodd.util.StringUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.server.dao.BankCodeDAO;
import org.server.withdraw.model.WithdrawChannel;
import org.server.exception.withdraw.MerchantDisabledException;
import org.server.exception.withdraw.MerchantNotFoundException;
import org.server.exception.withdraw.MerchantOrderNoDuplicateException;
import org.server.exception.withdraw.SignVerificationFailedException;
import org.server.exception.withdraw.WithdrawChannelBankNameIsRequiredException;
import org.server.mapper.MerchantMapper;
import org.server.mapper.WithdrawBankChannelMapper;
import org.server.mapper.WithdrawOrderMapper;
import org.server.service.OrderIdService;
import org.server.withdraw.dto.TraderResponseDto;
import org.server.withdraw.model.Merchant;
import org.server.withdraw.model.TraderResponseCode;
import org.server.withdraw.model.WithdrawOrder;
import org.server.withdraw.req.CreateWithdrawRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class WithdrawService {

  private static final String logPrefix = "【用戶提現下單】";


  @Resource
  private MerchantMapper merchantMapper;

  @Resource
  private WithdrawBankChannelMapper withdrawBankChannelMapper;

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

  public TraderResponseDto createWithdraw(CreateWithdrawRequest request, String ip)
      throws NoSuchFieldException, IllegalAccessException, MerchantNotFoundException, SignVerificationFailedException {
    log.info("{}createWithdraw: {}", logPrefix, request);

    // 商戶驗證(驗商戶代碼/驗簽)
    Merchant merchant = verifyRequest(request);

    TraderResponseDto dto;
    try {

      if (merchant.getStatus() == BankCodeDAO.STATUS_DISABLED) {
        log.info("{} merchant=[{}],merchant status is DISABLED", logPrefix,request.getUserId());
        throw new MerchantDisabledException();
      }

      if(StringUtil.isBlank(request.getBankName())){
        log.info("{} merchant=[{}],bankName is:{} ", logPrefix, request.getUserId() , request.getBankName());
        throw new WithdrawChannelBankNameIsRequiredException();
      }

      // 檢查訂單是否存在
      checkIfMerchantOrderNoAlreadyExists(request.getUserId(), request.getOrderNo());

      //檢查餘額
      TraderResponseCode responseCode = checkIfAmountIsSupported(merchant.getMerchantId(), request.getAmount());

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

//    限制同一卡號3分鐘內只能發一單
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
          .createTime(new Date())
          .updateTime(new Date())
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
      dto.sign(merchant.getRequestKey());
    }
    dto.sign(merchant.getRequestKey());
    return dto;
  }

  private Merchant verifyRequest(CreateWithdrawRequest request)
      throws NoSuchFieldException, IllegalAccessException, MerchantNotFoundException, SignVerificationFailedException {


    //TODO 一個 user 可以有多個商戶號 需要處理 (可能用商戶ID)
    Merchant merchant = merchantMapper.selectByUserId(request.getUserId());

    //Merchant對照
    if (merchant == null) {
      System.out.println("拋異常 沒有該商戶 銀行");
      throw new MerchantNotFoundException();
    }

    //暫時無
//    request.setVerifyCode(withdrawOrderVerifyCode);

    //驗證傳來的 簽名(對方已將物件屬性簽名) 核對 我們db裡存的 requestKey(依照物件屬性重簽)
    if (!request.verify(merchant.getRequestKey())) {
      log.info("本地 OrderNo: {}, msg: {}", request.getOrderNo(), TraderResponseCode.SIGN_VERIFICATION_FAILED.getMessage());
      throw new SignVerificationFailedException();
    }
    return merchant;
  }

  private void checkIfMerchantOrderNoAlreadyExists(String merchantId, String orderNo)
      throws MerchantOrderNoDuplicateException {

    List<WithdrawOrder> withdrawOrders = withdrawOrderMapper.selectByMerchantIdAndBankOrderNo(
        merchantId, orderNo);

    if(withdrawOrders != null && !withdrawOrders.isEmpty()){
      log.info("{}訂單號重複試: {}", merchantId, orderNo);
      throw new MerchantOrderNoDuplicateException();

    }
  }



  //查詢是否可出款 包含餘額
  private TraderResponseCode checkIfAmountIsSupported(String merchantId, BigDecimal amount) {

    //TODO 可跟銀行code關聯
    WithdrawChannel dao = withdrawBankChannelMapper.getWithdrawChannelDaoByMerchantId(merchantId);

    if(dao == null ){
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
