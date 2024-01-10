package org.server.withdraw.runable;


import java.math.BigDecimal;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import lombok.extern.log4j.Log4j2;
import org.apache.http.conn.ConnectTimeoutException;
import org.server.mapper.WithdrawBankCardBlackMapper;
import org.server.mapper.WithdrawChannelBankCodeMapper;
import org.server.mapper.WithdrawChannelBankDetailMapper;
import org.server.security.SecurityValidator;
import org.server.withdraw.model.WithdrawBankCardBlack;
import org.server.withdraw.model.WithdrawChannel;
import org.server.mapper.WithdrawBankChannelMapper;
import org.server.mapper.WithdrawOrderMapper;
import org.server.withdraw.model.WithdrawChannelBankDetail;
import org.server.withdraw.model.WithdrawChannelBankCode;
import org.server.withdraw.model.WithdrawOrder;
import org.server.withdraw.sercive.ExecuteWithdrawService;
import org.server.withdraw.sercive.JoinService;
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

  @Resource
  private WithdrawChannelBankDetailMapper withdrawChannelBankDetailMapper;

  @Resource
  private WithdrawBankCardBlackMapper withdrawBankCardBlackMapper;

  @Resource
  private SecurityValidator securityValidator;

  @Resource
  private JoinService joinService;

  private WithdrawOrder withdrawOrder;

  public ProcessWithdrawOrderRunnable(WithdrawOrder withdrawOrder) {
    this.withdrawOrder = withdrawOrder;
  }

  @Override
  public void run() {
    //確認是否符合全部要求
    boolean isLegalData = false;

    try {
      log.info("{} WithdrawOrderId:{}, 開始送單", ExecuteWithdrawService.logPrefix, withdrawOrder.getWithdrawOrderId());
      // 檢查代付銀行是否可用
      checkIfBankIsSupported(withdrawOrder.getMerchantId(),withdrawOrder.getBankName());
      // 過濾商戶所設置的銀行卡黑名單
      checkBankCardBlackList(withdrawOrder.getMerchantId(),withdrawOrder.getPayeeCardNo());
      // 檢查帳號下的渠道可接受此訂單金額,若無則拋exception
      checkIfAmountIsSupported(withdrawOrder.getMerchantId(), withdrawOrder.getAmount());
      // 檢查銀行卡號是否合法
      checkIfCardNoIsAllowed(withdrawOrder.getPayeeCardNo());
      log.info("{} WithdrawOrderId:{}, 檢查參數完成", ExecuteWithdrawService.logPrefix, withdrawOrder.getWithdrawOrderId());
      //TODO

      String merchantId = withdrawOrder.getMerchantId();
      // 不指定代付渠道
      BigDecimal amount = withdrawOrder.getAmount();
      WithdrawChannel withdrawChannel = getWithdrawChannel(merchantId, amount, withdrawOrder.getBankName());



      isLegalData = true;


    } catch (Exception e) {
      //以下三種異常 不做處理 (可能是預期的問題)
      if (e instanceof SocketTimeoutException || e instanceof SocketException
          || e instanceof ConnectTimeoutException) {

      } else {
        // 送單失敗，回調商戶
        log.error("{} WithdrawOrderId:{}, exception:{}", ExecuteWithdrawService.logPrefix,
            withdrawOrder.getWithdrawOrderId(), e.getMessage(), e);
        processWithdrawOrderOnFail(isLegalData, withdrawOrder, e);
      }
    }
  }



  /**
   * 檢查銀行是否支援
   *
   * @param merchantId	商戶號
   * @param bankName		銀行名
   */
  private void checkIfBankIsSupported(String merchantId, String bankName) throws Exception {

    // 依merchant找出所有的代付帳號並檢查是否可用銀行
    List<WithdrawChannel> channelList = withdrawBankChannelMapper.findByMerchantIds(merchantId);

    //ˊ找不到可使用渠道
    if (channelList == null || channelList.size()==0) {
      //TODO  找不到可使用渠道異常
//      throw new XxPayTraderException(TraderResponseCode.WITHDRAW_CHANNEL_NOT_FOUND_USABLE);
    }

    for(WithdrawChannel channel: channelList) {

      List<WithdrawChannelBankCode> bankList = withdrawChannelBankCodeMapper.findByBankName(bankName);
      // 1.比對銀行名稱是否匹配
      if(bankList.size() > 0) {
        List<WithdrawChannelBankDetail> list = withdrawChannelBankDetailMapper.findByWithdrawBankChannelId(String.valueOf(channel.getWithdrawBankChannelId()));
        // 2.比對渠道是否有支援該銀行
        if(list.size() > 0) {
          Map<String, String> map  = new HashMap<>();
          for (WithdrawChannelBankDetail withdrawChannelBankDetail : list) {
            String withdrawChannelBankName = withdrawChannelBankDetail.getBankName();
            String withdrawChannelBankCode = withdrawChannelBankDetail.getBankCode();
            if(withdrawChannelBankName.equals(bankName)){
              if(map.containsKey(bankName)){
                String oldValue = map.get(bankName);
                map.put(bankName,oldValue + "," + withdrawChannelBankCode);
              }else {
                map.put(bankName,withdrawChannelBankCode);
              }
            }
          }
          //使用stream
//          Map<String, String> map = list.stream() .collect(Collectors.toMap(WithdrawChannelBank::getBankName, WithdrawChannelBank::getBankCode, (oldVal,newVal) -> oldVal + "," + newVal));
          if(map.get(bankName) != null){
            return;
          }
        }else {
          // t_withdraw_channel_bank 沒資料表示支持所有銀行
          return;
        }
      }
    }
    //TODO 拋異常
//    log.info("商戶={} ,可用代付渠道=[{}] ,皆不支持[{}]", merchantId ,
//        channelList.stream().collect(Collectors.toMap(WithdrawChannel::getAccountId, WithdrawChannel::getAccountName)), bankName);
//    throw new XxPayTraderException(TraderResponseCode.BANK_NOT_SUPPORTED, bankName);
  }

  /**
   * 檢查卡號是否在黑名單內
   *
   * @param merchantId	商戶號
   * @param bankCardNo	銀行卡號
   * @throws
   */
  private void checkBankCardBlackList(String merchantId, String bankCardNo) {
    List<WithdrawBankCardBlack> exist = withdrawBankCardBlackMapper.findByMerchantIdBankCardNo(merchantId,bankCardNo);
    if (exist != null && exist.size()>0) {
      // TODO 拋異常 卡號是黑名單
//      throw new XxPayTraderException(TraderResponseCode.WITHDRAW_CHANNEL_INVALID_CARD_NO, bankCardNo);
    }
  }

  /**
   * 检查金额是否支持
   *
   * @param merchantId	商户号
   * @param amount		交易金额
   * @throws
   */
  private void checkIfAmountIsSupported(String merchantId , BigDecimal amount){

    List<WithdrawChannel> withdrawChannelList1 = withdrawBankChannelMapper.findByMerchantIdAndAndAmount(
        merchantId,
        amount);

    if (withdrawChannelList1 == null || withdrawChannelList1.size() == 0) {
      //TODO 拋異常 渠道金額限制或餘額不足
//      throw new XxPayTraderException(TraderResponseCode.WITHDRAW_CHANNEL_UNAVAILABLE_AMOUNT);
    }



  }


  /**
   * 確認銀行卡是否合法 (包含黑名單)
   *
   * @param payeeCardNo	銀行卡號
   */
  private void checkIfCardNoIsAllowed(String payeeCardNo) {
    if (!securityValidator.verifyCardNo(payeeCardNo)) {
//      TODO 拋異常 不合法卡號
//      throw new XxPayTraderRuntimeException(
//          TraderResponseCode.WITHDRAW_CHANNEL_INVALID_CARD_NO,
//          payeeCardNo);

          }
  }

  /**
   * 从商户所设置的代付帐号中取得一个可来进行代付发起的渠道
   *
   * @param merchantId	商户号
   * @param amount		交易金额
   * @param bankName		银行名称
   * @return
   */
  private WithdrawChannel getWithdrawChannel(String merchantId, BigDecimal amount, String bankName) {


    // 依merchant找出所有的代付帳號並檢查是否可用銀行
    List<WithdrawChannel> channelList = withdrawBankChannelMapper.findByMerchantIds(merchantId);

    //ˊ找不到可使用渠道
    if (channelList == null || channelList.size()==0) {
      //TODO  找不到可使用渠道異常
//      throw new XxPayTraderException(TraderResponseCode.WITHDRAW_CHANNEL_NOT_FOUND_USABLE);
    }

    // 部份金流商未提供余额查询，但仍可做为代付渠道之使用
    List<WithdrawChannel> canUseWithdrawChannelList = new ArrayList<WithdrawChannel>();

    for (WithdrawChannel withdrawChannel : channelList) {
      log.info("getWithdrawChannel 找到代付渠道withdrawChannel={}, catchId={}", withdrawChannel.getWithdrawBankChannelId(), withdrawChannel.getWithdrawBankChannelCode());

      //渠道的方銀行訊息
      List<WithdrawChannelBankDetail> canSupportBanks = joinService.bankDetailJoinBankCode(withdrawChannel.getWithdrawBankChannelId());
//      List<WithdrawChannelBankDetail> channelBankDetails = withdrawChannelBankDetailMapper.findByWithdrawBankChannelId(withdrawChannel.getWithdrawBankChannelId());
      //有記錄的銀行
//      List<WithdrawChannelBankCode> canSupportBanks = withdrawChannelBankCodeMapper.findByBankCode(withdrawChannel.getWithdrawBankChannelId());
      log.info("getWithdrawChannel canSupportBanks size: {}", canSupportBanks.size());
      if(canSupportBanks.size() > 0){
        boolean isSupportBankName = false;
        for(WithdrawChannelBankDetail model : canSupportBanks){
          WithdrawChannelBankCode withdrawChannelBankCode = model.getWithdrawChannelBankCode();
          if(bankName.equals(withdrawChannelBankCode.getBankName())){
            withdrawChannel.setChannelBankCode(model.getBankCode());
            isSupportBankName = true;
            break;
          }
        }
        if(!isSupportBankName){
          // 此代付渠道不支援银行
          log.info("getWithdrawChannel merchantId={}, WithdrawChannel={}, not support bank={}, ignore this withdraw channel", merchantId, withdrawChannel.getBankChannelMerchantName(), bankName);
          continue;
        }
      }
      canUseWithdrawChannelList.add(withdrawChannel);
    }

    if(canUseWithdrawChannelList.isEmpty()) {
      log.info("getWithdrawChannel merchantId={}, can use WithdrawChannel is empty", merchantId);
      // TODOb 拋異常 未有任何可用的accountChannel
//      throw new XxPayTraderException(TraderResponseCode.WITHDRAW_CHANNEL_NOT_FOUND_USABLE , bankName);
    }
    //重新排列 list
    WithdrawChannel before = canUseWithdrawChannelList.get(0);
    Collections.shuffle(canUseWithdrawChannelList);
    WithdrawChannel withdrawChannel = canUseWithdrawChannelList.get(0);
    log.info("find WithdrawChannel random result, before=[{}], after=[{}]", before.getBankChannelMerchantName(), withdrawChannel.getBankChannelMerchantName());
    return withdrawChannel;
  }






  /**
   * 提款訂單處理失敗
   *
   * @param isLegalData	false＝創建失敗, true=支付失敗
   * @param withdrawOrder	代付訂單資訊
   * @param exception		錯誤
   */
  @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ , rollbackFor = Exception.class)
  public void processWithdrawOrderOnFail(boolean isLegalData, WithdrawOrder withdrawOrder, Exception exception) {

    //不符合要求 創建失敗
    if(!isLegalData){
      withdrawOrder.setStatus(WithdrawOrder.STATUS_FAILED_ON_PROCESSING);
    }
    //符合要求但是  支付失敗
    if(isLegalData){
      withdrawOrder.setStatus(WithdrawOrder.STATUS_FAILED_ON_WITHDRAWING);
    }

    withdrawOrder.setRemark(exception.getMessage());
    int i = withdrawOrderMapper.updateWithdrawOrder(withdrawOrder);

    //TODO




  }

}
