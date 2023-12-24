package org.server.controller.req.order;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IncreaseBalanceOrderReq {

  String userId ;

  BigDecimal price ;

  String paymentMethod;


}
