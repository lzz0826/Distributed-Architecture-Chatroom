package org.server.controller.rep.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import org.server.vo.OrderVO;

@Data
@Builder
public class LocalTransferOrderRep {

  @ApiModelProperty(value="訂單訊息")
  private OrderVO orderVO;

}
