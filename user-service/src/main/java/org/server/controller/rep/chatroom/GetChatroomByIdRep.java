package org.server.controller.rep.chatroom;


import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import org.server.vo.ChatroomVO;

@Data
@Builder
public class GetChatroomByIdRep {

  @ApiModelProperty(value="查詢的聊天室訊息")
  private ChatroomVO chatroomVO;

}
