package org.server.controller.rep.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import org.server.vo.CallBackOrderVO;
import org.server.vo.OrderVO;

@Data
@Builder
public class OrderCallbackRep {


  @ApiModelProperty(value="回調訂單訊息")
  private CallBackOrderVO callBackOrderVO;
}
