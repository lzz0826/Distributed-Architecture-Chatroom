package org.server.controller.rep.chatroom;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import org.server.vo.ChatroomVO;

@Data
@Builder
public class AddCacheRoomRep {
  @ApiModelProperty(value="新增的聊天室訊息")
  private ChatroomVO chatroomVO;
}
