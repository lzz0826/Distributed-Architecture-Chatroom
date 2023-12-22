package service;


import base.BaseTest;
import java.math.BigDecimal;
import javax.annotation.Resource;
import org.junit.Test;
import org.server.enums.OrderTypeEnums;
import org.server.enums.PaymentMethodEnum;
import org.server.exception.order.CreateOrderException;
import org.server.exception.order.OrderTypeException;
import org.server.exception.wallet.IncreaseBalanceException;
import org.server.exception.wallet.InsufficientBalanceException;
import org.server.exception.wallet.UserNotHasWalletException;
import org.server.sercice.IdGeneratorService;
import org.server.service.OrderService;
import org.server.vo.OrderVO;

public class OrderServiceTest extends BaseTest {

  @Resource
  private IdGeneratorService idGeneratorService;

  @Resource
  private OrderService orderService;


  @Test
  public void reduceBalanceByWalletIdTest()
      throws CreateOrderException, IncreaseBalanceException, InsufficientBalanceException, OrderTypeException, UserNotHasWalletException {

    String userId = "4159394896707931412";
    String walletId = "4174129606516002722";
    BigDecimal price = new BigDecimal(99999);
    PaymentMethodEnum paymentMethodEnum = PaymentMethodEnum.CREDIT_CARD;
    OrderTypeEnums orderTypeEnums = OrderTypeEnums.REDUCE;


    OrderVO order = orderService.createOrder(walletId,price, paymentMethodEnum,
        orderTypeEnums);

    System.out.println(order);

  }

  @Test
  public void increaseBalanceByWalletIdTest()
      throws CreateOrderException, IncreaseBalanceException, InsufficientBalanceException, OrderTypeException, UserNotHasWalletException {

    String userId = "4159394896707931412";
    String walletId = "4174129606516002722";
    BigDecimal price = new BigDecimal(122);
    PaymentMethodEnum paymentMethodEnum = PaymentMethodEnum.CREDIT_CARD;
    OrderTypeEnums orderTypeEnums = OrderTypeEnums.INCREASE;


    OrderVO order = orderService.createOrder(walletId,price, paymentMethodEnum,
        orderTypeEnums);

    System.out.println(order);

  }



}
