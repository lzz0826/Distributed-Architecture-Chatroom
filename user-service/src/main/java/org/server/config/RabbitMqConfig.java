package org.server.config;

import org.springframework.amqp.core.AnonymousQueue;
import org.springframework.amqp.core.Base64UrlNamingStrategy;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

  /**
   * 聊天室
   */
  public static final String EXCHANGE_MSG_NAME = "msg";
  @Bean
  public FanoutExchange exchangeMsg() {
    return new FanoutExchange(EXCHANGE_MSG_NAME);
  }

  @Bean
  public Queue queueMsg() {
    return new AnonymousQueue(new Base64UrlNamingStrategy("queueMsg"));
  }

  @Bean
  public Binding bindingMsg(@Qualifier("exchangeMsg") FanoutExchange exchangeMsg, @Qualifier("queueMsg") Queue queueMsg) {
    return BindingBuilder.bind(queueMsg).to(exchangeMsg);
  }


}
