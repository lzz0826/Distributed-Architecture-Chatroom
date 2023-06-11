package org.server.websocket.enums;

public enum EWsMsgType {

  Chatroom("chatroom");

  public final String code;

  EWsMsgType(String code) {
    this.code = code;
  }
}
