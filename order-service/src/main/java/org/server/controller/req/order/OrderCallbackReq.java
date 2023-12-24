package org.server.controller.req.order;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.server.enums.OrderStatusEnums;
import org.server.enums.OrderTypeEnums;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderCallbackReq {

  private String orderId;
  private BigDecimal price;
  private String orderStatusEnums;





}
