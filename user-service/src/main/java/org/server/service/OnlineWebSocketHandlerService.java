package org.server.service;

import org.server.websocket.OnlineWebSocketHandler;
import org.server.websocket.enums.EWsMsgType;
import org.springframework.stereotype.Service;

@Service
public class OnlineWebSocketHandlerService {
  private volatile OnlineWebSocketHandler onlineWebSocketHandler = null;

  public void getOnlineWebSocketHandler(String userId, EWsMsgType eWsMsgType,
      String chatroomId, String receiverUserId, String finalPath) {
    if (onlineWebSocketHandler == null) {
      synchronized (this) {
        if (onlineWebSocketHandler == null) {
          onlineWebSocketHandler = new OnlineWebSocketHandler();
        }
      }
    }
    onlineWebSocketHandler.setChatroom(userId, eWsMsgType, chatroomId, receiverUserId, finalPath);
  }

  public OnlineWebSocketHandler getOnlineWebSocketHandler(){
    if (onlineWebSocketHandler == null) {
      synchronized (this) {
        if (onlineWebSocketHandler == null) {
          onlineWebSocketHandler = new OnlineWebSocketHandler();
        }
      }
    }
    return onlineWebSocketHandler;
  }
}
