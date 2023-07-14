package org.server.controller.rep.blackList;

import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import org.server.vo.BlackLisVO;

@Data
@Builder
public class BlackListRep {
  @ApiModelProperty(value="黑名單 List)")
  public List<BlackLisVO> blackLis;

}
