package org.server.vo;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class OrderVO {

  private String id;

  private String userId;

  private String walletId;

  private BigDecimal price; // 改為 BigDecimal

  private String paymentMethod;

  private int type;

  private int status;

  private Date updateTime;

  private Date createTime;
  private UserVO userVO;

}
