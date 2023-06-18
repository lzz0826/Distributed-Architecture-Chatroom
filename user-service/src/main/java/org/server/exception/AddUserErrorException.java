package org.server.exception;

import org.server.common.BaseResp;
import org.server.common.StatusCode;

public class AddUserErrorException extends Exception{

  @org.springframework.web.bind.annotation.ExceptionHandler(AddUserErrorException.class)
  public BaseResp<?> handleException(AddUserErrorException ex){
    return BaseResp.fail(StatusCode.AddUserFail);
  }

}
