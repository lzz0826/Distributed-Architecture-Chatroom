package org.server.controller.req.order;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.server.enums.OrderTypeEnums;
import org.server.enums.PaymentMethodEnum;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class increaseBalanceOrderReq {

  String userId ;

  BigDecimal price ;

  String paymentMethod;


}
