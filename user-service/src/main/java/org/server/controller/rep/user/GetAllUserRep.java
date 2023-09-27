package org.server.controller.rep.user;


import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.server.controller.rep.BasePageRep;
import org.server.vo.UserVO;

@Builder
@Data
@EqualsAndHashCode(callSuper = true)
public class GetAllUserRep extends BasePageRep {

  @ApiModelProperty(value="聊天記錄 List)")
  private List<UserVO> userVOs;

}
