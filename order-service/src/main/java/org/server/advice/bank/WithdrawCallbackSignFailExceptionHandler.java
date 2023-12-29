package org.server.advice.bank;


import lombok.extern.log4j.Log4j2;
import org.server.common.BaseResp;
import org.server.common.StatusCode;
import org.server.exception.bank.WithdrawCallbackSignFailException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Log4j2
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class WithdrawCallbackSignFailExceptionHandler {

  @org.springframework.web.bind.annotation.ExceptionHandler(WithdrawCallbackSignFailException.class)
  public BaseResp<?> handleException(WithdrawCallbackSignFailException ex){
    return BaseResp.fail(StatusCode.WithdrawCallbackSignFail);
  }

}
