package org.server.dao;


import java.math.BigDecimal;
import java.time.LocalDateTime;
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

  private String targetUserId;

  private String targetWalletId;

  private BigDecimal price;

  private int paymentMethod;

  private Integer type;

  private Integer status;

  private Date updateTime;

  private Date createTime;

  private LocalDateTime localDateTime;







}
