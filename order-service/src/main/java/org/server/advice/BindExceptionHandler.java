package org.server.advice;

import lombok.extern.log4j.Log4j2;
import org.server.common.BaseResp;
import org.server.common.StatusCode;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Log4j2
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class BindExceptionHandler {

  /**
   * 參數型別錯誤
   */
  @org.springframework.web.bind.annotation.ExceptionHandler(BindException.class)
  public BaseResp<?> handleException(BindException e) {
    return BaseResp.fail(StatusCode.BindExceptionError);

  }



}
