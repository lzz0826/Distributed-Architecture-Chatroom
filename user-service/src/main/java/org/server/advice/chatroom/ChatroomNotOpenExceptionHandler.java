package org.server.advice.chatroom;

import lombok.extern.log4j.Log4j2;
import org.server.common.BaseResp;
import org.server.common.StatusCode;
import org.server.exception.ChatroomNotOpenException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Log4j2
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ChatroomNotOpenExceptionHandler {

  @org.springframework.web.bind.annotation.ExceptionHandler(ChatroomNotOpenException.class)
  public BaseResp<?> handleException(ChatroomNotOpenException ex){
    return BaseResp.fail(StatusCode.ChatroomNotOpen);
  }


}
