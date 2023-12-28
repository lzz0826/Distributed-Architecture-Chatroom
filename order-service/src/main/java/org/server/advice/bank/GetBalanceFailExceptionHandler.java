package org.server.advice.bank;

import lombok.extern.log4j.Log4j2;
import org.server.common.BaseResp;
import org.server.common.StatusCode;
import org.server.exception.bank.GetBalanceFailException;
import org.server.exception.order.CallBackProcessingException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Log4j2
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GetBalanceFailExceptionHandler {

  @org.springframework.web.bind.annotation.ExceptionHandler(GetBalanceFailException.class)
  public BaseResp<?> handleException(GetBalanceFailException ex){
    return BaseResp.fail(StatusCode.GetBalanceFail);
  }

}
