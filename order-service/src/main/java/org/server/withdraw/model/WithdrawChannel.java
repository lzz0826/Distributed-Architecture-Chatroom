package org.server.withdraw.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WithdrawChannel {

  /**
   * 出款渠道ID
   */
  private String withdrawBankChannelId;

  /**
   * 賬號名稱
   */
  private String withdrawBankChannelName;

  /**
   * 出款渠道代碼(緩存和 實現抽象後的銀行代碼)
   */
  private String withdrawBankChannelCatchId;

  /**
   * 商戶ID
   */
  private String merchantId;

  /**
   * 使用者帳號
   */
  private String userId;

  /**
   * 状态，0:下架 1:启用 2:达标 3:风控 4:暫時禁用 5:冷却 6:金额冷却
   */
  private Integer status;

  /**
   * 銀行方提供給我方的ID
   */
  private String bankChannelMerchantId;

  /**
   * 銀行方提供給我方的名稱
   */
  private String bankChannelMerchantName;

  /**
   * 渠道額外參數，JSON格式
   */
  private String bankChannelExtra;

  /**
   * 出款公鑰
   */
  private String publicKey;

  /**
   * 出付私鑰
   */
  private String privateKey;

  /**
   * 登入賬號
   */
  private String loginUsername;

  /**
   * 登入密碼
   */
  private String loginPassword;

  /**
   * 單筆最低金額，單位分
   */
  private Long minAmount;

  /**
   * 單筆最高金額，單位分
   */
  private Long maxAmount;

  /**
   * 單日最高金額 - 達標金額，單位分
   */
  private Long dayMaxAmount;

  /**
   * 單日最多筆數 - 達標筆數
   */
  private Long dayMaxCount;

  /**
   * 累積收款金額，單位分
   */
  private Long totalAmount;

  /**
   * 累積收款筆數
   */
  private Long totalCount;

  /**
   * 當日累績收款金額，單位分
   */
  private Long todayAmount;

  /**
   * 當日累積收款筆數
   */
  private Long todayCount;

  /**
   * 備註
   */
  private String memo;

  /**
   * 成本，費率
   */
  private Double costRate;

  /**
   * 成本，固定金額，單位分
   */
  private Long costFixedAmount;

  /**
   * 賬號餘額，單位分
   */
  private BigDecimal balance;

  /**
   * 回調地址
   */
  private String notifyUrl;

  /**
   * 第三方銀行地區碼
   */
  private Integer bankAreaCode;

  /**
   * 第三方銀行聯行碼
   */
  private String bankCode;

  /**
   * 渠道開啟時段
   */
  private Timestamp openStartTime;

  /**
   * 渠道開啟時段
   */
  private Timestamp openEndTime;

  /**
   * 創建時間
   */
  private Timestamp createTime;

  /**
   * 更新時間
   */
  private Timestamp updateTime;

  /**
   * 創建人
   */
  private String creator;

  /**
   * 更新人
   */
  private String updater;



  private String channelBankCode; // 渠道銀行代碼


}
