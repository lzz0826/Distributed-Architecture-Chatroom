package org.server.enums;

public enum OrderType {


  INCREASE("increase",1),
  REDUCE("reduce",2);


  public final String name;

  public final int code;



  OrderType(String name , int code) {
    this.name = name;
    this.code = code;
  }

}
