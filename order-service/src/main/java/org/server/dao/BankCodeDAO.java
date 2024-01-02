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
public class BankCodeDAO {

  public static final int STATUS_DISABLED = 0;


  private String bankId;

  private String bankCode;

  private String bankAbbreviationn;

  private String bankName;

  //请求私钥
  //TODO DB mapper
  private String requestKey; //簽名key

  private String publicKey; // rsa公钥

  private String privateKey; // rsa私钥

  private Integer status; //銀行狀態 0 停用

  private Date updateTime;

  private Date createTime;

  private String creator;

  private String updater;


}
