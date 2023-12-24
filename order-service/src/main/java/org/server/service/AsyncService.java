package org.server.service;


import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AsyncService {

  @Async("orderServiceExecutor")
  public void callOtherServer(String orderId,String userId , BigDecimal price) {
    System.out.println("模擬其他服務等待.......開始");
    try {
      TimeUnit.SECONDS.sleep(10);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }

    System.out.println("模擬其他服務等待.......結束");


  }

}
