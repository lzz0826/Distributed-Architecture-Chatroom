package org.server.dao;


import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderDAO {

  private String id;

  private String userId;

  private String walletId;

  private int price;

  private String paymentMethod;

  private int type;

  private int status;

  private Date updateTime;

  private Date createTime;






}
