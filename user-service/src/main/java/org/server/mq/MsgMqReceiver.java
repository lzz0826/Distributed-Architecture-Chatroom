package org.server.mq;

import com.alibaba.fastjson.JSON;
import javax.annotation.Resource;
import org.server.service.OnlineWebSocketHandlerService;
import org.server.websocket.OnlineWebSocketHandler;
import org.server.websocket.entity.WsReq;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "#{queueMsg.name}")
public class MsgMqReceiver {

  @Resource
  private OnlineWebSocketHandlerService onlineWebSocketHandlerService;

    @RabbitHandler
    public void receive(String message) {
      WsReq<String> wsReq = JSON.parseObject(message, WsReq.class);
      OnlineWebSocketHandler onlineWebSocketHandler = onlineWebSocketHandlerService.getOnlineWebSocketHandler();
      System.out.println("接收 : "+wsReq);
      onlineWebSocketHandler.getChatroomMq(wsReq);
    }
}
