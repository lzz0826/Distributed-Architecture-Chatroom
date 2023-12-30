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

  private String bankId;

  private String bankCode;

  private String bankAbbreviationn;

  private String bankName;

  private Date updateTime;

  private Date createTime;

  private String creator;

  private String updater;


}
