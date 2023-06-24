package org.server.websocket.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.server.common.StatusCode;
import org.server.websocket.enums.EMsgType;
import org.server.websocket.enums.EWsMsgType;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WsRep<T> {


  /**
   * 消息種類
   */
  private EWsMsgType eWsMsgType;

  /**
   * 消息類型
   */
  protected EMsgType eMsgType;

  /**
   * 回傳值
   */
  private T response ;

  private StatusCode statusCode;


  /**
   * 接收者
   */
  private String receiverUserId;

  /**
   * 發送者
   */
  private String senderUserId;



  /**
   * 聊天室ID
   */
  private String chatroomId;




  public String getMsg() {
    if(statusCode == null){
      return null;
    }
    return statusCode.msg;
  }







}
