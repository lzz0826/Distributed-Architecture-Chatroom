package org.server.advice;

import lombok.extern.log4j.Log4j2;
import org.server.common.BaseResp;
import org.server.common.StatusCode;
import org.server.exception.NotOrderUserException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Log4j2
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class NotOrderUserExceptionHandler {

  /**
   * 找不到訂單
   */
  @org.springframework.web.bind.annotation.ExceptionHandler(NotOrderUserException.class)
  public BaseResp<?> handleException(NotOrderUserException e) {
    return BaseResp.fail(StatusCode.NotFoundOrder);
  }

}
