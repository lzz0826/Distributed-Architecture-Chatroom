package org.server.advice.withdraw;


import lombok.extern.log4j.Log4j2;
import org.server.common.BaseResp;
import org.server.exception.withdraw.SignVerificationFailedException;
import org.server.exception.withdraw.WithdrawChannelBankNameIsRequiredException;
import org.server.withdraw.model.TraderResponseCode;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Log4j2
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SignVerificationFailedExceptionHandler {
  @org.springframework.web.bind.annotation.ExceptionHandler(SignVerificationFailedException.class)
  public BaseResp<?> handleException(SignVerificationFailedException ex){
    return BaseResp.fail(TraderResponseCode.SIGN_VERIFICATION_FAILED);
  }

}
