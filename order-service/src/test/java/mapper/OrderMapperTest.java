package mapper;


import java.util.Date;
import org.server.mapper.OrderMapper;
import org.server.dao.OrderDAO;
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

    String name= "to3333ny";

    int price = 500;
    int i = orderMapper.updateByName(name,price);

    System.out.println(i);

  }

  @Test
  public void selectByIdTest(){
    OrderDAO order = orderMapper.selectById("2890589042718397264");
    System.out.println(order);
  }

  @Test
  public void selectAllTest(){
    List<OrderDAO> orders = orderMapper.selectAll();
    System.out.println(orders);
  }

  @Test
  public void insertOrderTest(){
    OrderDAO tony = OrderDAO
        .builder()
        .id(idGeneratorService.getNextId())
        .userId(idGeneratorService.getNextId())
        .walletId(idGeneratorService.getNextId())
        .price(200)
        .paymentMethod("paymentMethod")
        .type(1)
        .status(2)
        .createTime(new Date())
        .updateTime(new Date())
        .build();
    int i = orderMapper.insertOrder(tony);
    System.out.println(i);
  }

}
