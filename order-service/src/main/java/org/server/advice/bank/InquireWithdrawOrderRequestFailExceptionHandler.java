package org.server.advice.bank;

import lombok.extern.log4j.Log4j2;
import org.server.common.BaseResp;
import org.server.common.StatusCode;
import org.server.exception.bank.InquireWithdrawOrderRequestFailException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Log4j2
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class InquireWithdrawOrderRequestFailExceptionHandler {

  @org.springframework.web.bind.annotation.ExceptionHandler(InquireWithdrawOrderRequestFailException.class)
  public BaseResp<?> handleException(InquireWithdrawOrderRequestFailException ex){
    return BaseResp.fail(StatusCode.InquireWithdrawOrderRequestFail);
  }


}
