package org.server.advice.user;

import lombok.extern.log4j.Log4j2;
import org.server.common.BaseResp;
import org.server.common.StatusCode;
import org.server.exception.MissingParameterErrorException;
import org.server.exception.NotFoundUserException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Log4j2
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class NotFoundUserExceptionHandler {

  @org.springframework.web.bind.annotation.ExceptionHandler(NotFoundUserException.class)
  public BaseResp<?> handleException(NotFoundUserException ex){
    return BaseResp.fail(StatusCode.NotFoundUser);
  }

}
