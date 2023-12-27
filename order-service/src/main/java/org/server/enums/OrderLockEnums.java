package org.server.enums;

import org.apache.commons.lang3.StringUtils;

public enum OrderLockEnums {



  CALLBACK("ORDER:CALLBACK:");

  public final String name;

  OrderLockEnums(String name ) {
    this.name = name;
  }


  public static OrderLockEnums parse(String name) {
    if(!StringUtils.isBlank(name)){
      for(OrderLockEnums info : values()){
        if(info.name.equals(name)){
          return info;
        }
      }
    }
    return null;
  }

}
