import com.alibaba.fastjson2.JSON;
import mapper.BaseTest;
import org.junit.Test;
import org.server.client.controller.OkHttpController;
import org.server.client.obj.Order;
import org.server.client.obj.Results;

public class OkHttpControllerTest extends BaseTest {

  @Test
  public void testTest99 (){
    OkHttpController okHttpController = new OkHttpController();
    String s = okHttpController.test99();
    System.out.println(s);
  }


  @Test
  public void testSendMsg(){
    OkHttpController okHttpController = new OkHttpController();
    String msg = okHttpController.sendMsg();
    System.out.println(msg);
  }


  @Test
  public void testSendForm(){
    OkHttpController okHttpController = new OkHttpController();
    String msg = okHttpController.sendForm();
    System.out.println(msg);
    Order o = JSON.parseObject(msg, Order.class);
    System.out.println(o.getUserId());

  }


  @Test
  public void testSendOder(){
    OkHttpController okHttpController = new OkHttpController();
    String msg = okHttpController.sendOder();
    System.out.println(msg);

  }

  @Test
  public void testSendResult(){
    OkHttpController okHttpController = new OkHttpController();
    Results results = okHttpController.sendResult();
    System.out.println(results.getCode());
    System.out.println(results.getStatus());
    Order order = new Order();
    if(results.getData() instanceof Order){
      order = (Order) results.getData();
    }else {
      System.out.println("Data is not of type Order.");
    }
    System.out.println(order.getUserVO().getId());


  }

}
