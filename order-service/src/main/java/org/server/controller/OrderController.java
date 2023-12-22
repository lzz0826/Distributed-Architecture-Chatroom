package org.server.controller;


import java.math.BigDecimal;
import javax.annotation.Resource;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.server.common.BaseResp;
import org.server.controller.req.order.increaseBalanceOrderReq;
import org.server.dao.WalletsDAO;
import org.server.enums.OrderTypeEnums;
import org.server.enums.PaymentMethodEnum;
import org.server.exception.ErrorParameterErrorException;
import org.server.exception.MissingParameterErrorException;
import org.server.exception.order.CreateOrderException;
import org.server.exception.order.OrderTypeException;
import org.server.exception.wallet.IncreaseBalanceException;
import org.server.exception.wallet.InsufficientBalanceException;
import org.server.exception.wallet.UserNotHasWalletException;
import org.server.service.OrderService;
import org.server.service.WalletService;
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
  public BaseResp<OrderVO> increaseBalanceOrder(@RequestBody increaseBalanceOrderReq req)
      throws MissingParameterErrorException, ErrorParameterErrorException,
      UserNotHasWalletException, CreateOrderException, IncreaseBalanceException,
      InsufficientBalanceException, OrderTypeException {

    if(StringUtils.isBlank(req.getUserId())
        || req.getPrice() == null
        || StringUtils.isBlank(req.getPaymentMethod())
    ) {
      throw new MissingParameterErrorException();
    }

    PaymentMethodEnum paymentMethodEnum = PaymentMethodEnum.parse(req.getPaymentMethod());
    OrderTypeEnums orderTypeEnums = OrderTypeEnums.INCREASE;

    if(paymentMethodEnum == null || orderTypeEnums == null){
      throw new ErrorParameterErrorException();
    }

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
  public BaseResp<OrderVO> reduceBalanceOrder(@RequestBody increaseBalanceOrderReq req)
      throws MissingParameterErrorException, ErrorParameterErrorException,
      UserNotHasWalletException, CreateOrderException, IncreaseBalanceException,
      InsufficientBalanceException, OrderTypeException {

    if(StringUtils.isBlank(req.getUserId())
        || req.getPrice() == null
        || StringUtils.isBlank(req.getPaymentMethod())
    ) {
      throw new MissingParameterErrorException();
    }

    PaymentMethodEnum paymentMethodEnum = PaymentMethodEnum.parse(req.getPaymentMethod());
    OrderTypeEnums orderTypeEnums = OrderTypeEnums.REDUCE;

    if(paymentMethodEnum == null || orderTypeEnums == null){
      throw new ErrorParameterErrorException();
    }

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








}


