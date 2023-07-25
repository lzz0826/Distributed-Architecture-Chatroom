package org.server.controller.rep.chatroom;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import org.server.vo.ChatroomVO;

@Data
@Builder
public class UpdateCacheRoomRep {


  @ApiModelProperty(value="聊天室更新的後的訊息")
  private ChatroomVO chatroomVO;


}
