package org.server.controller.rep.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import org.server.vo.OrderVO;
import org.server.vo.WalletsVO;

@Data
@Builder
public class IncreaseBalanceOrderRep {

  @ApiModelProperty(value="訂單訊息")
  private OrderVO orderVO;

}
