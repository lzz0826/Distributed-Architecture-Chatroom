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
import org.server.mapper.BankCodeMapper;
import org.server.mapper.OrderMapper;
import org.server.mapper.WithdrawOrderMapper;
import org.server.service.WalletService;
import org.server.utils.FormDataUtil;
import org.server.withdraw.dto.TraderResponseDto;
import org.server.withdraw.dto.WithdrawVerifyOrderDto;
import org.server.withdraw.model.Merchant;
import org.server.withdraw.model.TraderResponseCode;
import org.server.withdraw.model.WithdrawOrder;
import org.server.withdraw.req.CreateWithdrawRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Log4j2
@Service
public class WithdrawService {

  @Resource
  private BankCodeMapper bankCodeMapper;
  @Resource
  private OrderMapper orderMapper;

  @Resource
  private WalletService walletService;

  @Resource
  private WithdrawOrderMapper withdrawOrderMapper;

  //如果有配置使用配置(:true 預設 false)
  @Value("${withdraw.order.verify.enable:false}")
  private Boolean withdrawOrderVerifyEnable;

  //配置同一張卡在某段最大請求數的 (時間 分)限制
  @Value("${withdraw.order.continuous.minute:10}")
  private Integer withdrawContinuousMinute;

  //同一張卡在某段時間的最大請求的 (數)限制
  @Value("${withdraw.order.continuous.count:3}")
  private Integer withdrawContinuousCount;

  @Value("${withdraw.order.verify.minute.enable:true}")
  private Boolean withdrawOrderVerifyMinuteEnable;




  private void createWithdraw(CreateWithdrawRequest request, String ip)
      throws NoSuchFieldException, IllegalAccessException {
    String logPrefix = "【用戶提现下单】";
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

      // 检查订单是否存在
      checkIfUserOrderNoAlreadyExists(request.getUserId(), request.getOrderNo());
      TraderResponseCode responseCode = checkIfAmountIsSupported(request.getUserId(), request.getAmount());


      //初始 status 生成中
      int status = WithdrawOrder.STATUS_COMPLETED_ING;
      String remark = null;

      //TraderResponseCode != SUCCESS
      if(!responseCode.getCode().equals(TraderResponseCode.SUCCESS.getCode())){
        status = WithdrawOrder.STATUS_FAILED_ON_PROCESSING;
        remark = responseCode.getMessage();

        // SUCCESS 後是否驗籤
      } else {
        //是否在二次驗籤 (二次验证接口) 暫時無
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

      //TODO
      //限制同一卡号3分钟内只能发一单
//      log.info("限制同一卡号3分钟内只能发一单检查开启:{}", withdrawOrderVerifyMinuteEnable);
//      if(withdrawOrderVerifyMinuteEnable){
//        String redisKey = "withdrawOrder:PayeeCardNo:" + request.getPayeeCardNo();
//        String payeeCardNo = redisTemplate.opsForValue().get(redisKey);
//        if(StringUtils.isNotBlank(payeeCardNo)){
//          log.info("{} merchant=[{}],PayeeCardNo is:{}, please try again after 3 min.", logPrefix, merchant.getMerchantId(), payeeCardNo);
//          status = WithdrawOrder.STATUS_FAILED_ON_PROCESSING;
//          remark = TraderResponseCode.WITHDRAW_METHOD_PAYEE_CARD_NO_BANK_CONTINUOUS.getMessage();
//          responseCode = TraderResponseCode.WITHDRAW_METHOD_PAYEE_CARD_NO_BANK_CONTINUOUS;
//        }else{
//          if (status == WithdrawOrder.STATUS_PENDING) {
//            redisTemplate.opsForValue().set(redisKey, request.getPayeeCardNo());
//            redisTemplate.expire(redisKey, 3, TimeUnit.MINUTES);
//            log.info("{} set redis key:{} 3min", logPrefix, redisKey);
//          }
//        }
//      }




    }catch (Exception e){
      e.printStackTrace();
    }



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
      log.info("{}使用者订单号重复试: {}", userId, orderNo);
//      拋異常
//      TraderResponseCode.MERCHANT_ORDER_NO_DUPLICATE,
    }
  }


  //查詢是否可出款 包含餘額
  private TraderResponseCode checkIfAmountIsSupported(String userId, BigDecimal amount) {

    WalletsDAO walletsDAO = walletService.getWalletByUserId(userId);
    if(walletsDAO != null ){
      //找不到 錢包
      return TraderResponseCode.WITHDRAW_CHANNEL_NOT_FOUND_USABLE;
    }

    if(walletsDAO.getBalance().compareTo(BigDecimal.ZERO) <= 0){
      return TraderResponseCode.WITHDRAW_CHANNEL_BALANCE_NOT_ENOUGH;
    }

    if(walletsDAO.getBalance().compareTo(amount)  <= 0){
      return TraderResponseCode.WITHDRAW_CHANNEL_BALANCE_NOT_ENOUGH;
    }

    return TraderResponseCode.SUCCESS;
  }

}
