package org.server.websocket.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.server.websocket.enums.EMsgType;
import org.server.websocket.enums.EWsMsgType;
import org.springframework.security.core.parameters.P;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WsReq<T> {

  /**
   * 發送者的ID
   */
  private String senderUserId;

  /**
   * 私聊的對象ID
   */
  private String receiverUserId;

  /**
   * 聊天室ID
   */
  private String chatroomId;

  /**
   * 消息種類
   */
  private EWsMsgType eWsMsgType;
  /**
   * 消息類型
   */
  private EMsgType eMsgType;

  /**
   * 傳遞值
   */
  private T request ;






}
