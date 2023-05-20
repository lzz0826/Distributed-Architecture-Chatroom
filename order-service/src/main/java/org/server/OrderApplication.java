package org.server;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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
  * */
  @Bean
  public RestTemplate restTemplate(){
    return new RestTemplate();
  }


}