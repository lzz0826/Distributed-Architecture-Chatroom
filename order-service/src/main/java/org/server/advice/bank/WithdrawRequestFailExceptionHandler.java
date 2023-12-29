package org.server.advice.bank;


import lombok.extern.log4j.Log4j2;
import org.server.common.BaseResp;
import org.server.common.StatusCode;
import org.server.exception.bank.WithdrawOrderNotExistException;
import org.server.exception.bank.WithdrawRequestFailException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Log4j2
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class WithdrawRequestFailExceptionHandler {


  @org.springframework.web.bind.annotation.ExceptionHandler(WithdrawRequestFailException.class)
  public BaseResp<?> handleException(WithdrawRequestFailException ex){
    return BaseResp.fail(StatusCode.WithdrawRequestFail);
  }

}
