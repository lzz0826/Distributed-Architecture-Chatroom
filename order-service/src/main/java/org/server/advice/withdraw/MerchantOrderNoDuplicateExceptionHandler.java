package org.server.advice.withdraw;

import lombok.extern.log4j.Log4j2;
import org.server.common.BaseResp;
import org.server.exception.withdraw.MerchantOrderNoDuplicateException;
import org.server.withdraw.model.TraderResponseCode;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Log4j2
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MerchantOrderNoDuplicateExceptionHandler {

  @org.springframework.web.bind.annotation.ExceptionHandler(MerchantOrderNoDuplicateException.class)
  public BaseResp<?> handleException(MerchantOrderNoDuplicateException ex){
    return BaseResp.fail(TraderResponseCode.MERCHANT_ORDER_NO_DUPLICATE);
  }

}
