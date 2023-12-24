package org.server.vo;


import java.math.BigDecimal;
import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CallBackOrderVO {
  private String orderId;

  private BigDecimal price;

  private int type;

  private int status;

  private Date updateTime;



}
