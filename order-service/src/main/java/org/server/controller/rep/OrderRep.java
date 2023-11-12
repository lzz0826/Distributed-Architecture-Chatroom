package org.server.controller.rep;

import lombok.Builder;
import lombok.Data;
import org.server.vo.OrderVO;

@Data
@Builder
public class OrderRep {
  private OrderVO orderVO;

}
