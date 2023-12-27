package org.server.advice.order;

import lombok.extern.log4j.Log4j2;
import org.server.common.BaseResp;
import org.server.common.StatusCode;
import org.server.exception.order.CallBackProcessingException;
import org.server.exception.order.OrderTypeException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Log4j2
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CallBackProcessingExceptionHandler {

  @org.springframework.web.bind.annotation.ExceptionHandler(CallBackProcessingException.class)
  public BaseResp<?> handleException(CallBackProcessingException ex){
    return BaseResp.fail(StatusCode.CallBackProcessing);
  }

}
