package org.server.withdraw.runable;


import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import lombok.extern.log4j.Log4j2;
import org.apache.http.conn.ConnectTimeoutException;
import org.server.mapper.WithdrawBankCardBlackMapper;
import org.server.mapper.WithdrawChannelBankCodeMapper;
import org.server.mapper.WithdrawChannelBankMapper;
import org.server.withdraw.model.WithdrawBankCardBlack;
import org.server.withdraw.model.WithdrawChannel;
import org.server.mapper.WithdrawBankChannelMapper;
import org.server.mapper.WithdrawOrderMapper;
import org.server.withdraw.model.WithdrawChannelBank;
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

  @Resource
  private WithdrawChannelBankMapper withdrawChannelBankMapper;

  @Resource
  private WithdrawBankCardBlackMapper withdrawBankCardBlackMapper;

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
      // 檢查銀行卡號是否合法
      log.info("{} WithdrawOrderId:{}, 檢查參數完成", ExecuteWithdrawService.logPrefix, withdrawOrder.getWithdrawOrderId());




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
      // 1.比對銀行名稱是否匹配
      if(bankList.size() > 0) {
        List<WithdrawChannelBank> list = withdrawChannelBankMapper.findByWithdrawBankChannelId(String.valueOf(channel.getWithdrawBankChannelId()));
        // 2.比對渠道是否有支援該銀行
        if(list.size() > 0) {
          Map<String, String> map  = new HashMap<>();
          for (WithdrawChannelBank withdrawChannelBank : list) {
            String withdrawChannelBankName = withdrawChannelBank.getBankName();
            String withdrawChannelBankCode = withdrawChannelBank.getBankCode();
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
