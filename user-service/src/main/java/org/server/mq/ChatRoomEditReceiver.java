package org.server.mq;

import com.alibaba.fastjson.JSON;
import javax.annotation.Resource;
import org.server.exception.chatroom.ChatroomNotOpenException;
import org.server.service.ChatroomService;
import org.server.websocket.entity.ChatRoomReq;
import org.server.websocket.enums.ChatRoomEditType;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "#{queueChatRoomEdit.name}")
public class ChatRoomEditReceiver {

  @Resource
  private ChatroomService chatroomService;


  @RabbitHandler
  public void receive(String message) {
    ChatRoomReq chatRoomReq = JSON.parseObject(message, ChatRoomReq.class);
    System.out.println("接收 : "+chatRoomReq);

    ChatRoomEditType chatRoomEditType = chatRoomReq.getChatRoomEditType();
    switch (chatRoomEditType) {
      case Join:
        System.out.println("MQ加入聊天室");
        chatroomService.joinChatroomCache(chatRoomReq.getChatRoomId(),chatRoomReq.getUserId());
        break;
      case Quit:
        System.out.println("MQ退出聊天室指定");
        chatroomService.leaveChatroomCache(chatRoomReq.getChatRoomId(),chatRoomReq.getUserId());
        break;
      case QuitAll:
        System.out.println("MQ退出聊天室(全)");
        chatroomService.leaveChatroomAllCache(chatRoomReq.getUserId());
        break;
    }

  }

}
