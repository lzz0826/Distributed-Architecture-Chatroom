package service;


import base.BaseTest;
import java.math.BigDecimal;
import javax.annotation.Resource;
import org.junit.Test;
import org.server.enums.OrderType;
import org.server.enums.PaymentMethod;
import org.server.exception.MissingParameterErrorException;
import org.server.exception.order.CreateOrderException;
import org.server.exception.order.OrderTypeException;
import org.server.exception.wallet.IncreaseBalanceException;
import org.server.exception.wallet.InsufficientBalanceException;
import org.server.sercice.IdGeneratorService;
import org.server.service.OrderService;
import org.server.service.WalletService;
import org.server.vo.OrderVO;
import org.server.vo.WalletsVO;

public class OrderServiceTest extends BaseTest {

  @Resource
  private IdGeneratorService idGeneratorService;

  @Resource
  private OrderService orderService;


  @Test
  public void reduceBalanceByWalletIdTest()
      throws CreateOrderException, IncreaseBalanceException, InsufficientBalanceException, OrderTypeException {

    String userId = "4159394896707931412";
    String walletId = "4172351591389963076";
    BigDecimal price = new BigDecimal(122);
    PaymentMethod paymentMethod  = PaymentMethod.CREDIT_CARD;
    OrderType orderType = OrderType.REDUCE;


    OrderVO order = orderService.createOrder(userId,walletId,price,paymentMethod,orderType);

    System.out.println(order);

  }



}
