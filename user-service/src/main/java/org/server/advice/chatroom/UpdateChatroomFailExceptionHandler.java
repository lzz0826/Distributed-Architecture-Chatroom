package org.server.advice.chatroom;

import lombok.extern.log4j.Log4j2;
import org.server.common.BaseResp;
import org.server.common.StatusCode;
import org.server.exception.chatroom.NotFoundChatroomException;
import org.server.exception.chatroom.UpdateChatroomFailException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Log4j2
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class UpdateChatroomFailExceptionHandler {

  @org.springframework.web.bind.annotation.ExceptionHandler(UpdateChatroomFailException.class)
  public BaseResp<?> handleException(UpdateChatroomFailException ex){
    return BaseResp.fail(StatusCode.UpdateChatroomFail);
  }

}
