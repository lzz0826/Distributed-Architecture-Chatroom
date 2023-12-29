package org.server.advice.bank;

import lombok.extern.log4j.Log4j2;
import org.server.common.BaseResp;
import org.server.common.StatusCode;
import org.server.exception.bank.GetBalanceRequestFailException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Log4j2
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GetBalanceRequestFailExceptionHandler {

  @org.springframework.web.bind.annotation.ExceptionHandler(GetBalanceRequestFailException.class)
  public BaseResp<?> handleException(GetBalanceRequestFailException ex){
    return BaseResp.fail(StatusCode.GetBalanceRequestFail);
  }

}
