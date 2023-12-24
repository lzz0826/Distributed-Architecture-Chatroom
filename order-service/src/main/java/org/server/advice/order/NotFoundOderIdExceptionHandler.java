package org.server.advice.order;

import lombok.extern.log4j.Log4j2;
import org.server.common.BaseResp;
import org.server.common.StatusCode;
import org.server.exception.order.NotFoundOderIdException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Log4j2
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class NotFoundOderIdExceptionHandler {

  @org.springframework.web.bind.annotation.ExceptionHandler(NotFoundOderIdException.class)
  public BaseResp<?> handleException(NotFoundOderIdException ex){
    return BaseResp.fail(StatusCode.NotFoundOrder);
  }

}
