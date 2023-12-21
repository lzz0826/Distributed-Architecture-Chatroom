package org.server.controller.rep.wallet;


import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import org.server.vo.WalletsVO;

@Data
@Builder
public class InitUserWalletRep {

  @ApiModelProperty(value="錢包訊息")
  private WalletsVO walletsVO;

}
