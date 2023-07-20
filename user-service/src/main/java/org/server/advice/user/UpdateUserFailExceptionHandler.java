package org.server.advice.user;

import lombok.extern.log4j.Log4j2;
import org.server.common.BaseResp;
import org.server.common.StatusCode;
import org.server.exception.UpdateUserFailException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Log4j2
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class UpdateUserFailExceptionHandler {
  @org.springframework.web.bind.annotation.ExceptionHandler(UpdateUserFailException.class)
  public BaseResp<?> handleException(UpdateUserFailException ex){
    return BaseResp.fail(StatusCode.UpdateUserFail);
  }

}

