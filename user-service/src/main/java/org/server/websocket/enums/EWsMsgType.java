package org.server.websocket.enums;

import org.apache.commons.lang3.StringUtils;

public enum EWsMsgType {

  Welcome("welcome"),

  NotLogin("notLogin"),

  IsSilence("isSilence"),

  HeartBeatPong("heartBeatPong"),

  Chatroom("chatroom"),

  PrivateChat("privateChat"),

  All("all");


  public final String code;

  EWsMsgType(String code) {
    this.code = code;
  }

  public static EWsMsgType parse(String code) {
    if (!StringUtils.isBlank(code)) {
      for (EWsMsgType info : values()) {
        if (info.code.equals(code)) {
          return info;
        }
      }
    }
    return null;
  }




}
