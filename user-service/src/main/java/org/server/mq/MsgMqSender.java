package org.server.mq;

import static org.server.config.RabbitMqConfig.EXCHANGE_MSG_NAME;

import com.alibaba.fastjson.JSON;
import javax.annotation.Resource;
import org.server.websocket.entity.WsRep;
import org.server.websocket.entity.WsReq;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;

@Component
public class MsgMqSender {
    @Resource
    private AmqpTemplate amqpTemplate;
    public void send(WsReq<?> wsReq) {
        try {
            String msg = JSON.toJSONString(wsReq);
            System.out.println("發送 : "+msg);

            amqpTemplate.convertAndSend(EXCHANGE_MSG_NAME, "", msg);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
