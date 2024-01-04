package org.server.advice.withdraw;

import lombok.extern.log4j.Log4j2;
import org.server.common.BaseResp;
import org.server.exception.withdraw.MerchantNotFoundException;
import org.server.withdraw.model.TraderResponseCode;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Log4j2
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MerchantNotFoundExceptionHandler {
  @org.springframework.web.bind.annotation.ExceptionHandler(MerchantNotFoundException.class)
  public BaseResp<?> handleException(MerchantNotFoundException ex){
    return BaseResp.fail(TraderResponseCode.MERCHANT_NOT_FOUND);
  }

}
