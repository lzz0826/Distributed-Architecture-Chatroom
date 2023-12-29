package org.server.withdraw;

import java.math.BigDecimal;
import java.util.Map;
import org.server.withdraw.model.HttpRequestEnum;
import org.server.withdraw.model.WithdrawMethodResponse;
import org.server.withdraw.model.WithdrawNotifyResponse;
import org.server.withdraw.model.WithdrawOrder;

public interface WithdrawBank {

  /**
   * 判定是否可以使用此銀行
   *
   * @param bankId 代付渠道代碼(tb_pay_bank_code.bank_code_id)
   * @return
   */
  boolean canSupport(String bankId);

  /**
   * 是否支持餘額查詢
   * @return
   */
  boolean canDoQueryBalance();

  /**
   * 向銀行查詢餘額 並更新本地餘額
   * @param withdrawBank
   * @return
   * @throws Exception
   */
  BigDecimal getBalance(org.server.withdraw.model.WithdrawBank withdrawBank) throws Exception;

  /**
   * 向銀行發起取款(轉帳)請求
   * @param withdrawBank
   * @param withdrawOrder
   * @return
   * @throws Exception
   */
  WithdrawMethodResponse execute(org.server.withdraw.model.WithdrawBank withdrawBank, WithdrawOrder withdrawOrder)
      throws Exception;

  /**
   * 向銀行查詢訂單狀況 並更新本地數據
   * getOrder調用時機: <br> 1. [spring-common] WithdrawNotifyService (接收到第三方回調request時調用)<br> 2.
   * [schedule] ScheduleWithdrawService (排程定時同步代付訂單時調用)<br> 3. [trade] QueryWithdrawService
   * (後台MGR代付訂單手動更新時調用)
   *
   * @param withdrawOrder
   * @param withdrawBank
   * @return
   * @throws Exception
   */
  WithdrawMethodResponse getOrder(WithdrawOrder withdrawOrder, org.server.withdraw.model.WithdrawBank withdrawBank,
      HttpRequestEnum enums) throws Exception;

  /**
   * 僅驗回調簽名
   *
   * @param withdrawBank
   * @param params
   * @return
   * @throws Exception
   */
  WithdrawNotifyResponse checkNotifySign(org.server.withdraw.model.WithdrawBank withdrawBank,
      Map<String, String> params)
      throws Exception;

  /**
   * 出款失敗是否要扣手續費
   *
   * @return
   */
  boolean shouldChargeFeeOnFailed();

  /**
   * 回傳銀行訂單出款成功的定義代碼
   * @return
   */
  String getThirdOrderSuccessCode();


}
