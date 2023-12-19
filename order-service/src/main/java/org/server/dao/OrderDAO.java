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
public class OrderDAO {

  private String id;

  private String userId;

  private String walletId;

  private BigDecimal price; // 改為 BigDecimal

  private String paymentMethod;

  private int type;

  private int status;

  private Date updateTime;

  private Date createTime;






}
