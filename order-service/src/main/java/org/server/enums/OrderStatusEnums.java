package org.server.enums;

import org.apache.commons.lang3.StringUtils;

public enum OrderStatusEnums {



  CREATE("create",0),
  PAYING("paying",1),
  SUCCESS("success",2),
  FAIL("fail",3);

  public final String name;
  public final int code;



  OrderStatusEnums(String name , int code) {
    this.name = name;
    this.code = code;
  }

  public static OrderStatusEnums parse(Integer code) {
    if (code != null) {
      values();
      for (OrderStatusEnums info : values()) {
        if (info.code == code) {
          return info;
        }
      }
    }
    return null;
  }

  public static OrderStatusEnums parse(String name) {
    if(!StringUtils.isBlank(name)){
      for(OrderStatusEnums info : values()){
        if(info.name.equals(name)){
          return info;
        }
      }
    }
    return null;
  }

}
