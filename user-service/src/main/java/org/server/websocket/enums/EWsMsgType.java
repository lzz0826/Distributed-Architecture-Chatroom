package org.server.websocket.enums;

public enum EWsMsgType {

  Chatroom("chatroom"),

  PrivateChat("privateChat"),

  All("all");


  public final String code;

  EWsMsgType(String code) {
    this.code = code;
  }
}
