package org.server.advice.Interpersonal;

import lombok.extern.log4j.Log4j2;
import org.server.common.BaseResp;
import org.server.common.StatusCode;
import org.server.exception.Interpersonal.AddInterpersonalFailException;
import org.server.exception.chatroomRecord.NeedPageException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Log4j2
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AddInterpersonalFailExceptionHandler {

  @org.springframework.web.bind.annotation.ExceptionHandler(AddInterpersonalFailException.class)
  public BaseResp<?> handleException(AddInterpersonalFailException ex){
    return BaseResp.fail(StatusCode.AddInterpersonalFail);
  }

}
