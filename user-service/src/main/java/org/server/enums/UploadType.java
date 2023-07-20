package org.server.enums;

public enum UploadType {

  Image("image"),

  Avatar("avatar"),

  File("file");

  public final String code;

  UploadType(String code) {
    this.code = code;
  }


}
