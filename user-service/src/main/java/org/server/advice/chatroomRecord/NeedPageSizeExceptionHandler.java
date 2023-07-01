package org.server.advice.chatroomRecord;

import lombok.extern.log4j.Log4j2;
import org.server.common.BaseResp;
import org.server.common.StatusCode;
import org.server.exception.chatroomRecord.NeedPageSizeException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Log4j2
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class NeedPageSizeExceptionHandler {
  @org.springframework.web.bind.annotation.ExceptionHandler(NeedPageSizeException.class)
  public BaseResp<?> handleException(NeedPageSizeException ex){
    return BaseResp.fail(StatusCode.NeedPageSize);
  }

}
