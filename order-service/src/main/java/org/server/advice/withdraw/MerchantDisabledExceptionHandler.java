package org.server.advice.withdraw;

import lombok.extern.log4j.Log4j2;
import org.server.common.BaseResp;
import org.server.exception.withdraw.MerchantDisabledException;
import org.server.withdraw.model.TraderResponseCode;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Log4j2
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MerchantDisabledExceptionHandler {

  @org.springframework.web.bind.annotation.ExceptionHandler(MerchantDisabledException.class)
  public BaseResp<?> handleException(MerchantDisabledException ex){
    return BaseResp.fail(TraderResponseCode.MERCHANT_DISABLED);
  }

}
