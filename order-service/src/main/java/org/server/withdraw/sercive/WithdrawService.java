package org.server.withdraw.sercive;

import java.math.BigDecimal;
import java.util.List;
import javax.annotation.Resource;
import jodd.util.StringUtil;
import lombok.extern.log4j.Log4j2;
import org.server.dao.BankCodeDAO;
import org.server.dao.OrderDAO;
import org.server.dao.WalletsDAO;
import org.server.mapper.BankCodeMapper;
import org.server.mapper.OrderMapper;
import org.server.service.WalletService;
import org.server.withdraw.dto.TraderResponseDto;
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

  //如果有配置使用配置(:true 預設 true)
  @Value("${withdraw.order.verify.enable:true}")
  private Boolean withdrawOrderVerifyEnable;


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
        if(withdrawOrderVerifyEnable){

          //TODO

        }


      }



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
