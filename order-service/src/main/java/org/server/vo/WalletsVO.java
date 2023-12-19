package org.server.vo;

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
public class WalletsVO {
  private String walletId;
  private String userId;
  private BigDecimal balance;
  private Integer status;
  private Date updateTime;
  private Date createTime;

}

