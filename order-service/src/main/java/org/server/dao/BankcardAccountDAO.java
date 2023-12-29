package org.server.dao;


import java.math.BigDecimal;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BankcardAccountDAO {
  //存款卡ID(相當於銀行帳號)
  private String bankCardAccountId;

  //銀行ID
  private String bankId;

  //存款卡編號(相當於銀行帳號)
  private String cardId;

  //類型: 0:收款卡, 1:中轉卡, 2:安全卡, 3:付款卡
  private int type;

  //用戶名
  private String payeeCardName;

  //銀行卡號(相當於信用卡)
  private String payeeCardNo;

  //銀行名稱
  private String bankName;

  //地區名稱
  private String bankArea;

  //分行名稱
  private String branchName;

  //狀態 0:下架 1:啟用 2:達標 3:風控 4:暫時禁用 5:冷卻 6:金額冷卻
  private int status;

  //備註
  private String memo;

  //卡片餘額
  private BigDecimal balance;

  //登入帳號
  private String loginUsername;

  //登入密碼
  private String loginPassword;

  //交易密碼
  private String transactionPassword;

  //手機號碼
  private String mobile;

  //建立時間
  private Date createTime;

  //更新時間
  private Date updateTime;

  //創建人
  private String creator;

  //更新人
  private String updater;

}
