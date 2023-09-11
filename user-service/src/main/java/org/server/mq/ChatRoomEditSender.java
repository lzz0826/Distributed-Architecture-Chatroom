package org.server.mq;


import static org.server.config.RabbitMqConfig.EXCHANGE_CHATROOM_EDIT_NAME;

import com.alibaba.fastjson.JSON;
import javax.annotation.Resource;
import org.server.websocket.entity.ChatRoomReq;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;

@Component

public class ChatRoomEditSender {

  @Resource
  private AmqpTemplate amqpTemplate;
  public void send(ChatRoomReq chatRoomReq) {
    try {
      String msg = JSON.toJSONString(chatRoomReq);
      System.out.println("發送 : "+msg);

      amqpTemplate.convertAndSend(EXCHANGE_CHATROOM_EDIT_NAME, "", msg);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

}
