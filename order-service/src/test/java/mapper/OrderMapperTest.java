package mapper;


import base.BaseTest;
import java.math.BigDecimal;
import java.util.Date;
import org.server.enums.OrderTypeEnums;
import org.server.enums.PaymentMethodEnum;
import org.server.mapper.OrderMapper;
import org.server.dao.OrderDAO;
import org.server.sercice.IdGeneratorService;
import java.util.List;
import javax.annotation.Resource;
import org.junit.Test;

public class OrderMapperTest extends BaseTest {

  @Resource
  private OrderMapper orderMapper;

  @Resource
  private IdGeneratorService idGeneratorService;


  @Test
  public void test(){

    String userId= "4159389780461786807";

    BigDecimal bigDecimal = new BigDecimal(55.5551);
    int i = orderMapper.updateByUserId(userId,bigDecimal);

    System.out.println(i);

  }

  @Test
  public void selectByIdTest(){
    OrderDAO order = orderMapper.selectById("4159387311728139580");
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
        .price(new BigDecimal(100.222222))
        .paymentMethod(PaymentMethodEnum.CREDIT_CARD.code)
        .type(OrderTypeEnums.REDUCE.code)
        .status(2)
        .createTime(new Date())
        .updateTime(new Date())
        .build();
    int i = orderMapper.insertOrder(tony);
    System.out.println(i);
  }

  @Test
  public void updateByOrderIdTest(){
    OrderDAO tony = OrderDAO
        .builder()
        .id("4174409193049763562")
        .userId("9999")
        .walletId("9999")
        .targetWalletId("9999")
        .targetUserId("99999")
        .price(new BigDecimal(100.222222))
        .paymentMethod(PaymentMethodEnum.CREDIT_CARD.code)
        .type(OrderTypeEnums.REDUCE.code)
        .status(2)
        .createTime(new Date())
        .updateTime(new Date())
        .build();
    int i = orderMapper.updateByOrderId(tony);
    System.out.println(i);
  }


}
