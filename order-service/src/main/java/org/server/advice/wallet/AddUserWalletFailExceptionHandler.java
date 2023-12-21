package org.server.advice.wallet;

import lombok.extern.log4j.Log4j2;
import org.server.common.BaseResp;
import org.server.common.StatusCode;
import org.server.exception.wallet.AddUserWalletFailException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Log4j2
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AddUserWalletFailExceptionHandler {


  /**
   * 添加錢包失敗
   */
  @org.springframework.web.bind.annotation.ExceptionHandler(AddUserWalletFailException.class)
  public BaseResp<?> handleException(AddUserWalletFailException e) {
    return BaseResp.fail(StatusCode.AddUserWalletFail);
  }

}
