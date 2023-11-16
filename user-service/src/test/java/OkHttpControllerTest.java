import com.alibaba.fastjson2.JSON;
import mapper.BaseTest;
import org.junit.Test;
import org.server.client.controller.OkHttpController;
import org.server.client.obj.Order;
import org.server.client.obj.Results;
import org.server.common.BaseResp;

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
    BaseResp<Object> baseResp = BaseResp.builder().build();
    Order order = Order.builder().build();
    try{
      baseResp = JSON.parseObject(msg, BaseResp.class);
    }catch (Exception e){
      System.out.println("解析JSON BaseResp失敗");
    }
    try{
      order = JSON.parseObject(msg, Order.class);

    }catch (Exception e){
      System.out.println("解析JSON Order失敗");

    }
    Integer statusCode = baseResp.getStatusCode();
    String mst = baseResp.getMst();

    System.out.println(order.toString());
    System.out.println(statusCode);
    System.out.println(mst);

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
  @Test
  public void testSendImg (){
    OkHttpController okHttpController = new OkHttpController();
    String s = okHttpController.sendImg();
    System.out.println(s);
  }

  @Test
  public void testSendSyncImg (){
    OkHttpController okHttpController = new OkHttpController();
    okHttpController.sendSyncImg();
  }


}
