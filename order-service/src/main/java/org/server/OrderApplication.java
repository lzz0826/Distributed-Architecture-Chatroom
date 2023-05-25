package org.server;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@EnableScheduling
@SpringBootApplication
public class OrderApplication {

  public static void main(String[] args) {
    SpringApplication.run(OrderApplication.class, args);
    System.out.println("Hello world!");
  }


  /**
   * 創建 RestTemplate 並注入Spring 容器
   * @LoadBalanced 負載均衡
  * */
  @Bean
  @LoadBalanced
  public RestTemplate restTemplate(){
    return new RestTemplate();
  }


//  //全局配置隨機 負載均衡 (需要針對各為服務到yml)
//  @Bean
//  public IRule randomRule(){
//    return new RandomRule();
//  }


}