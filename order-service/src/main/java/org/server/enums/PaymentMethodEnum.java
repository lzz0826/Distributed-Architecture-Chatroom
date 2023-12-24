package org.server.enums;

import org.apache.commons.lang3.StringUtils;
import org.server.exception.ErrorParameterErrorException;

public enum PaymentMethodEnum {

  CREDIT_CARD("CreditCard", 1),
  TRANSFER_MONEY("TransferMoney", 2);

  public final String name;
  public final int code;

  PaymentMethodEnum(String name, int code) {
    this.name = name;
    this.code = code;
  }


  public int getCode() {
    return code;
  }

  public String getName() {
    return name;
  }

  public static PaymentMethodEnum parse(Integer code) {
    if (code != null) {
      for (PaymentMethodEnum info : values()) {
        if (info.code == code) {
          return info;
        }
      }
    }
    return null;
  }

  public static PaymentMethodEnum parse(String name) {
    if(!StringUtils.isBlank(name)){
      for(PaymentMethodEnum info : values()){
        if(info.name.equals(name)){
          return info;
        }
      }
    }
    return null;
  }

  public static PaymentMethodEnum checkPayment(String paymentMethod) throws ErrorParameterErrorException {
    PaymentMethodEnum paymentMethodEnum = PaymentMethodEnum.parse(paymentMethod);
    if(paymentMethodEnum == null){
      throw new ErrorParameterErrorException();
    }
    return paymentMethodEnum;
  }



}
