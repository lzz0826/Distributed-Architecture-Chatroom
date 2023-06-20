package org.server.advice.system;

import lombok.extern.log4j.Log4j2;
import org.server.common.BaseResp;
import org.server.common.StatusCode;
import org.server.exception.AddErrorException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Log4j2
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AddErrorExceptionHandler {

  @org.springframework.web.bind.annotation.ExceptionHandler(AddErrorException.class)
  public BaseResp<?> handleException(AddErrorException ex){
    return BaseResp.fail(StatusCode.AddFail);
  }


}
