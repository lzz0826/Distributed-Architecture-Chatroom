package org.server.advice.withdraw;

import lombok.extern.log4j.Log4j2;
import org.server.common.BaseResp;
import org.server.exception.withdraw.MerchantDisabledException;
import org.server.exception.withdraw.WithdrawChannelBankNameIsRequiredException;
import org.server.withdraw.model.TraderResponseCode;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Log4j2
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class WithdrawChannelBankNameIsRequiredExceptionHandler {

  @org.springframework.web.bind.annotation.ExceptionHandler(WithdrawChannelBankNameIsRequiredException.class)
  public BaseResp<?> handleException(WithdrawChannelBankNameIsRequiredException ex){
    return BaseResp.fail(TraderResponseCode.WITHDRAW_CHANNEL_BANK_NAME_IS_REQUIRED);
  }


}
