package org.server.advice.chatroom;


import lombok.extern.log4j.Log4j2;
import org.server.common.BaseResp;
import org.server.common.StatusCode;
import org.server.exception.chatroom.NeedChatroomNameException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Log4j2
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class NeedChatroomNameExceptionHandler {


  @org.springframework.web.bind.annotation.ExceptionHandler(NeedChatroomNameException.class)
  public BaseResp<?> handleException(NeedChatroomNameException ex){
    return BaseResp.fail(StatusCode.NeedChatroomName);
  }

}
