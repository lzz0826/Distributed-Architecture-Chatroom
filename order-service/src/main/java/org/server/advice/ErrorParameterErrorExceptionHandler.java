package org.server.advice;

import lombok.extern.log4j.Log4j2;
import org.server.common.BaseResp;
import org.server.common.StatusCode;
import org.server.exception.ErrorParameterErrorException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Log4j2
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ErrorParameterErrorExceptionHandler {

  @org.springframework.web.bind.annotation.ExceptionHandler(ErrorParameterErrorException.class)
  public BaseResp<?> handleException(ErrorParameterErrorException ex){
    return BaseResp.fail(StatusCode.ErrorParameter);
  }

}
