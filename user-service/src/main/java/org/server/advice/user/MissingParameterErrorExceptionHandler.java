package org.server.advice.user;

import lombok.extern.log4j.Log4j2;
import org.server.common.BaseResp;
import org.server.common.StatusCode;
import org.server.exception.AddUserErrorException;
import org.server.exception.MissingParameterErrorException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Log4j2
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MissingParameterErrorExceptionHandler {

  @org.springframework.web.bind.annotation.ExceptionHandler(MissingParameterErrorException.class)
  public BaseResp<?> handleException(MissingParameterErrorException ex){
    return BaseResp.fail(StatusCode.MissingParameter);
  }

}
