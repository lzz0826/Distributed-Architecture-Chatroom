package org.server.controller;


import java.math.BigDecimal;
import javax.annotation.Resource;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.server.common.BaseResp;
import org.server.common.StatusCode;
import org.server.controller.req.order.IncreaseBalanceOrderReq;
import org.server.controller.req.order.OrderCallbackReq;
import org.server.controller.req.order.TransferOrderReq;
import org.server.dao.WalletsDAO;
import org.server.enums.OrderStatusEnums;
import org.server.enums.OrderTypeEnums;
import org.server.enums.PaymentMethodEnum;
import org.server.exception.ErrorParameterErrorException;
import org.server.exception.MissingParameterErrorException;
import org.server.exception.order.CreateOrderException;
import org.server.exception.order.NotFoundOderIdException;
import org.server.exception.order.OrderStatusException;
import org.server.exception.order.OrderTypeException;
import org.server.exception.wallet.IncreaseBalanceException;
import org.server.exception.wallet.InsufficientBalanceException;
import org.server.exception.wallet.ReduceBalanceException;
import org.server.exception.wallet.UserNotHasWalletException;
import org.server.service.OrderService;
import org.server.service.WalletService;
import org.server.vo.CallBackOrderVO;
import org.server.vo.OrderVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
@Log4j2
public class OrderController {


  @Resource
  private OrderService orderService;

  @Resource
  private WalletService walletService;

  @PostMapping("/increaseBalanceOrder")
  public BaseResp<OrderVO> increaseBalanceOrder(@RequestBody IncreaseBalanceOrderReq req)
      throws MissingParameterErrorException, ErrorParameterErrorException,
      UserNotHasWalletException, CreateOrderException, InsufficientBalanceException {
    if(StringUtils.isBlank(req.getUserId())
        || req.getPrice() == null
        || StringUtils.isBlank(req.getPaymentMethod())
    ) {
      throw new MissingParameterErrorException();
    }

    PaymentMethodEnum paymentMethodEnum = checkPayment(req.getPaymentMethod());
    OrderTypeEnums orderTypeEnums = OrderTypeEnums.INCREASE;

    String userId = req.getUserId();
    BigDecimal price = req.getPrice();

    WalletsDAO walletsDAO = walletService.getWalletByUserId(userId);

    if(walletsDAO == null){
      throw new UserNotHasWalletException();
    }

    String walletId = walletsDAO.getWalletId();
    OrderVO order = orderService.createOrder(walletId,price,paymentMethodEnum,orderTypeEnums);

    return BaseResp.ok(order);
  }

  @PostMapping("/reduceBalanceOrder")
  public BaseResp<OrderVO> reduceBalanceOrder(@RequestBody IncreaseBalanceOrderReq req)
      throws MissingParameterErrorException, ErrorParameterErrorException,
      UserNotHasWalletException, CreateOrderException, InsufficientBalanceException {

    if(StringUtils.isBlank(req.getUserId())
        || req.getPrice() == null
        || StringUtils.isBlank(req.getPaymentMethod())
    ) {
      throw new MissingParameterErrorException();
    }

    PaymentMethodEnum paymentMethodEnum = checkPayment(req.getPaymentMethod());
    OrderTypeEnums orderTypeEnums = OrderTypeEnums.REDUCE;


    String userId = req.getUserId();
    BigDecimal price = req.getPrice();

    WalletsDAO walletsDAO = walletService.getWalletByUserId(userId);

    if(walletsDAO == null){
      throw new UserNotHasWalletException();
    }

    String walletId = walletsDAO.getWalletId();
    OrderVO order = orderService.createOrder(walletId,price,paymentMethodEnum,orderTypeEnums);

    return BaseResp.ok(order);
  }

  //TODO
  @PostMapping("/transferOrder")
  public BaseResp<String> transferOrder(@RequestBody TransferOrderReq req)
      throws MissingParameterErrorException {
    if(StringUtils.isBlank(req.getUserId())
        || StringUtils.isBlank(req.getWalletId())
        || StringUtils.isBlank(req.getTargetUserId())
        || StringUtils.isBlank(req.getTargetWalletId())
        || req.getPrice() == null
        || StringUtils.isBlank(req.getPaymentMethod())
    ) {
      throw new MissingParameterErrorException();
    }

    return BaseResp.ok("order");
  }

  @PostMapping("/orderCallback")
  public BaseResp<CallBackOrderVO> orderCallback(@RequestBody OrderCallbackReq req )
      throws MissingParameterErrorException, IncreaseBalanceException, OrderTypeException, OrderStatusException, NotFoundOderIdException, ReduceBalanceException, UserNotHasWalletException {
    if(StringUtils.isBlank(req.getOrderId())
        || req.getPrice() == null
        || req.getOrderStatusEnums() == null
    ) {
      throw new MissingParameterErrorException();
    }

    String orderId = req.getOrderId();
    BigDecimal price = req.getPrice();

    OrderStatusEnums orderStatusEnums = OrderStatusEnums.parse(req.getOrderStatusEnums());

    if(orderStatusEnums == null){
      throw new OrderStatusException();
    }

    CallBackOrderVO vo = orderService.callBackOrder(orderId, price, orderStatusEnums);
    return BaseResp.ok(vo, StatusCode.Success);
  }


  private PaymentMethodEnum checkPayment(String paymentMethod) throws ErrorParameterErrorException {
    PaymentMethodEnum paymentMethodEnum = PaymentMethodEnum.parse(paymentMethod);
    if(paymentMethodEnum == null){
      throw new ErrorParameterErrorException();
    }
    return paymentMethodEnum;
  }







}


