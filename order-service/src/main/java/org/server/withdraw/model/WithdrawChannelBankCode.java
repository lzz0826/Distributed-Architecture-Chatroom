package org.server.withdraw.model;


import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WithdrawChannelBankCode {

  private String bankCodeId; // 出款銀行代碼ID

  private String bankCode; // 銀行聯行碼

  private String bankName; // 銀行名稱

  private Integer status; // 狀態 0:禁用 1:啟用

  private String memo; //備註

  private Date updateTime;

  private Date createTime;



}
