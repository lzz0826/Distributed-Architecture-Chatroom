package org.server.websocket.enums;

public enum EMsgType {


  System("system"),

  App("app"),
  HeartBeat("heartBeat");

  public final String code;

  EMsgType(String code) {
    this.code = code;
  }
}
