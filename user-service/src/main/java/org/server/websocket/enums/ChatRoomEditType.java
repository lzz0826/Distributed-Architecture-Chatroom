package org.server.websocket.enums;

public enum ChatRoomEditType {


  Join("join"),
  Quit("quit"),
  QuitAll("quitAll"),

  Silence("silence")
  ;





  public final String code;

  ChatRoomEditType(String code) {
    this.code = code;
  }

}
