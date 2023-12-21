package org.server.enums;

public enum PaymentMethod {

  CREDIT_CARD("CreditCard", 1),
  TRANSFER_MONEY("TransferMoney", 2);

  public final String name;
  public final int code;

  PaymentMethod(String name, int code) {
    this.name = name;
    this.code = code;
  }

}
