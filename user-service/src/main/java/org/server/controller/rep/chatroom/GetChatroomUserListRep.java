package org.server.controller.rep.chatroom;

import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.server.controller.rep.BasePageRep;
import org.server.vo.UserVO;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class GetChatroomUserListRep extends BasePageRep {


  @ApiModelProperty(value="聊天室id")
  private String id;


  @ApiModelProperty(value="聊天室上在線人員")
  private List<UserVO> userVOs;


}
