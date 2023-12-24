package org.server.controller.req.order;

import java.math.BigDecimal;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferOrderReq {

  private String userId;

  private String walletId;

  private String targetUserId;

  private String targetWalletId;

  private BigDecimal price;

  private String paymentMethod;


}
