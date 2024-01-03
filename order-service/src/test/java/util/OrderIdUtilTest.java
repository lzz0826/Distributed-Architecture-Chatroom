package util;

import base.BaseTest;
import javax.annotation.Resource;
import org.junit.Test;
import org.server.service.OrderIdService;

public class OrderIdUtilTest extends BaseTest {

  @Resource
  private OrderIdService orderIdService;


  @Test
  public void test(){
    String withdrawId = orderIdService.getWithdrawId();
    System.out.println(withdrawId);
  }

}
