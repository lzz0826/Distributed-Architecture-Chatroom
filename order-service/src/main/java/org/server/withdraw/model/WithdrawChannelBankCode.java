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

  private String bankCodeId;

  private String withdrawBankChannelId;

  private String bankCode;

  private String bankName;

  private Date updateTime;

  private Date createTime;



}
