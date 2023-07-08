package org.server.advice.Interpersonal;

import lombok.extern.log4j.Log4j2;
import org.server.common.BaseResp;
import org.server.common.StatusCode;
import org.server.exception.Interpersonal.EditInterpersonalException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Log4j2
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class EditInterpersonalExceptionHandler {

  @org.springframework.web.bind.annotation.ExceptionHandler(EditInterpersonalException.class)
  public BaseResp<?> handleException(EditInterpersonalException ex){
    return BaseResp.fail(StatusCode.EditInterpersonalFail);
  }

}
