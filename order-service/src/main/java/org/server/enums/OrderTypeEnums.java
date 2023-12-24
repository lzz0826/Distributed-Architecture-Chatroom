package org.server.enums;

import org.apache.commons.lang3.StringUtils;

public enum OrderTypeEnums {


  INCREASE("increase",1),
  REDUCE("reduce",2),
  LOCAL_TRANSFER("localTransfer",3);


  public final String name;

  public final int code;


  OrderTypeEnums(String name , int code) {
    this.name = name;
    this.code = code;
  }

  public static OrderTypeEnums parse(Integer code) {
    if (code != null) {
      values();
      for (OrderTypeEnums info : values()) {
        if (info.code == code) {
          return info;
        }
      }
    }
    return null;
  }

  public static OrderTypeEnums parse(String name) {
    if(!StringUtils.isBlank(name)){
      for(OrderTypeEnums info : values()){
        if(info.name.equals(name)){
          return info;
        }
      }
    }
    return null;
  }

}
