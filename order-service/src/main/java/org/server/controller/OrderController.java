package org.server.controller;


import java.math.BigDecimal;
import javax.annotation.Resource;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.server.common.BaseResp;
import org.server.common.StatusCode;
import org.server.controller.rep.order.IncreaseBalanceOrderRep;
import org.server.controller.rep.order.LocalTransferOrderRep;
import org.server.controller.rep.order.OrderCallbackRep;
import org.server.controller.rep.order.ReduceBalanceOrderRep;
import org.server.controller.req.order.IncreaseBalanceOrderReq;
import org.server.controller.req.order.OrderCallbackReq;
import org.server.controller.req.order.TransferOrderReq;
import org.server.dao.WalletsDAO;
import org.server.enums.OrderStatusEnums;
import org.server.enums.OrderTypeEnums;
import org.server.enums.PaymentMethodEnum;
import org.server.exception.ErrorParameterErrorException;
import org.server.exception.MissingParameterErrorException;
import org.server.exception.order.CallBackProcessingException;
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

  //創建儲值訂單 需要回調才會改訂單狀態
  @PostMapping("/increaseBalanceOrder")
  public BaseResp<IncreaseBalanceOrderRep> increaseBalanceOrder(@RequestBody IncreaseBalanceOrderReq req)
      throws MissingParameterErrorException, ErrorParameterErrorException,
      UserNotHasWalletException, CreateOrderException, InsufficientBalanceException {
    if(StringUtils.isBlank(req.getUserId())
        || req.getPrice() == null
        || StringUtils.isBlank(req.getPaymentMethod())
    ) {
      throw new MissingParameterErrorException();
    }

    PaymentMethodEnum paymentMethodEnum = PaymentMethodEnum.checkPayment(req.getPaymentMethod());
    OrderTypeEnums orderTypeEnums = OrderTypeEnums.INCREASE;

    String userId = req.getUserId();
    BigDecimal price = req.getPrice();

    WalletsDAO walletsDAO = walletService.getWalletByUserId(userId);

    if(walletsDAO == null){
      throw new UserNotHasWalletException();
    }
    String walletId = walletsDAO.getWalletId();
    OrderVO vo = orderService.createOrder(walletId,price,paymentMethodEnum,orderTypeEnums);

    IncreaseBalanceOrderRep rep = IncreaseBalanceOrderRep
        .builder()
        .orderVO(vo)
        .build();

    return BaseResp.ok(rep,StatusCode.Success);
  }

  //創建扣款訂單 需要回調才會改訂單狀態
  @PostMapping("/reduceBalanceOrder")
  public BaseResp<ReduceBalanceOrderRep> reduceBalanceOrder(@RequestBody IncreaseBalanceOrderReq req)
      throws MissingParameterErrorException, ErrorParameterErrorException,
      UserNotHasWalletException, CreateOrderException, InsufficientBalanceException {

    if(StringUtils.isBlank(req.getUserId())
        || req.getPrice() == null
        || StringUtils.isBlank(req.getPaymentMethod())
    ) {
      throw new MissingParameterErrorException();
    }

    PaymentMethodEnum paymentMethodEnum = PaymentMethodEnum.checkPayment(req.getPaymentMethod());
    OrderTypeEnums orderTypeEnums = OrderTypeEnums.REDUCE;


    String userId = req.getUserId();
    BigDecimal price = req.getPrice();

    WalletsDAO walletsDAO = walletService.getWalletByUserId(userId);

    if(walletsDAO == null){
      throw new UserNotHasWalletException();
    }

    String walletId = walletsDAO.getWalletId();
    OrderVO vo = orderService.createOrder(walletId,price,paymentMethodEnum,orderTypeEnums);
    ReduceBalanceOrderRep rep = ReduceBalanceOrderRep
        .builder()
        .orderVO(vo)
        .build();

    return BaseResp.ok(rep);
  }

  @PostMapping("/localTransferOrder")
  public BaseResp<LocalTransferOrderRep> localTransferOrder(@RequestBody TransferOrderReq req)
      throws MissingParameterErrorException, ErrorParameterErrorException, CreateOrderException, ReduceBalanceException, IncreaseBalanceException, InsufficientBalanceException, UserNotHasWalletException {
    if(StringUtils.isBlank(req.getUserId())
        || StringUtils.isBlank(req.getWalletId())
        || StringUtils.isBlank(req.getTargetUserId())
        || StringUtils.isBlank(req.getTargetWalletId())
        || req.getPrice() == null
        || StringUtils.isBlank(req.getPaymentMethod())
    ) {
      throw new MissingParameterErrorException();
    }
    String userId = req.getUserId();
    String walletId = req.getWalletId();
    String targetUserId = req.getTargetUserId();
    String targetWalletId = req.getTargetWalletId();
    BigDecimal price = req.getPrice();
    PaymentMethodEnum paymentMethodEnum = PaymentMethodEnum.checkPayment(req.getPaymentMethod());

    OrderVO vo = orderService.localTransferOrder(userId, walletId, targetUserId, targetWalletId,
         price, paymentMethodEnum);
    LocalTransferOrderRep rep = LocalTransferOrderRep
        .builder()
        .orderVO(vo)
        .build();

    return BaseResp.ok(rep,StatusCode.Success);
  }


  /**
   * 回調 :
   * 目前是直接修改
   * 實際情況因該是要反查後在更新訂單狀態和修改錢包
   */
  @PostMapping("/orderCallback")
  public BaseResp<OrderCallbackRep> orderCallback(@RequestBody OrderCallbackReq req )
      throws MissingParameterErrorException, IncreaseBalanceException, OrderTypeException, OrderStatusException, NotFoundOderIdException, ReduceBalanceException, UserNotHasWalletException, CallBackProcessingException {
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

    CallBackOrderVO vo = orderService.orderCallback(orderId, price, orderStatusEnums);

    OrderCallbackRep rep = OrderCallbackRep
        .builder()
        .callBackOrderVO(vo)
        .build();
    return BaseResp.ok(rep, StatusCode.Success);
  }


}


