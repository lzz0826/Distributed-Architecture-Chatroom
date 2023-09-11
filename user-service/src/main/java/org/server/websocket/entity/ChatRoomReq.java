package org.server.websocket.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.server.common.StatusCode;
import org.server.websocket.enums.ChatRoomEditType;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomReq {

  /**
   * 聊天室ID
   */
  private String chatRoomId;

  /**
   * UserID
   */
  private String userId;

  /**
   * 方法類型
   */
  private ChatRoomEditType chatRoomEditType;

  private StatusCode statusCode;

}
