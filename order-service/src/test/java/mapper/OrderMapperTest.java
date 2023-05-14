package mapper;


import org.server.mapper.OrderMapper;
import org.server.pojo.Order;
import org.server.sercice.IdGeneratorService;
import java.util.List;
import javax.annotation.Resource;
import org.junit.Test;

public class OrderMapperTest extends BaseTest{

  @Resource
  private OrderMapper orderMapper;

  @Resource
  private IdGeneratorService idGeneratorService;

  @Test
  public void test(){
    Order order = orderMapper.selectById("2890589042718397264");
    System.out.println(order);
  }

  @Test
  public void selectAllTest(){
    List<Order> orders = orderMapper.selectAll();
    System.out.println(orders);
  }

  @Test
  public void insertOrderTest(){
    Order tony = Order
        .builder()
        .id(idGeneratorService.getNextId())
        .userId(idGeneratorService.getNextId())
        .name("tony")
        .price(200)
        .num(1)
        .build();
    int i = orderMapper.insertOrder(tony);
    System.out.println(i);

  }

}
