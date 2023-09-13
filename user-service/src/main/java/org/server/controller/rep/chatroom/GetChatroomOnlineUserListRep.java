package org.server.controller.rep.chatroom;


import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import org.server.vo.ChatroomOnlineUserVO;


@Data
@Builder
public class GetChatroomOnlineUserListRep {


  @ApiModelProperty(value="聊天室在線人員")
  public ChatroomOnlineUserVO chatroomOnlineUser;



}
