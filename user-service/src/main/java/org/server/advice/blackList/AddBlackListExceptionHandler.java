package org.server.advice.blackList;


import lombok.extern.log4j.Log4j2;
import org.server.common.BaseResp;
import org.server.common.StatusCode;
import org.server.exception.blackListException.AddBlackListException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Log4j2
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AddBlackListExceptionHandler {

  @org.springframework.web.bind.annotation.ExceptionHandler(AddBlackListException.class)
  public BaseResp<?> handleException(AddBlackListException ex){
    return BaseResp.fail(StatusCode.AddBlackListFail);
  }

}
