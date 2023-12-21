package org.server.advice.wallet;


import lombok.extern.log4j.Log4j2;
import org.server.common.BaseResp;
import org.server.common.StatusCode;
import org.server.exception.wallet.UserNotHasWalletException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Log4j2
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class UserNotHasWalletExceptionHandler {

  @org.springframework.web.bind.annotation.ExceptionHandler(UserNotHasWalletException.class)
  public BaseResp<?> handleException(UserNotHasWalletException ex){
    return BaseResp.fail(StatusCode.UserNotHasWallet);
  }

}
