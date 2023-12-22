package org.server.service;

import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Resource;
import lombok.extern.log4j.Log4j2;
import org.server.dao.WalletsDAO;
import org.server.enums.OrderTypeEnums;
import org.server.enums.PaymentMethodEnum;
import org.server.exception.order.CreateOrderException;
import org.server.exception.order.OrderTypeException;
import org.server.exception.wallet.IncreaseBalanceException;
import org.server.exception.wallet.InsufficientBalanceException;
import org.server.exception.wallet.UserNotHasWalletException;
import org.server.mapper.OrderMapper;
import org.server.dao.OrderDAO;
import org.server.sercice.IdGeneratorService;
import org.server.vo.OrderVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Log4j2
public class OrderService {


  @Resource
  private IdGeneratorService idGeneratorService;
  @Resource
  private OrderMapper orderMapper;

  @Resource
  private WalletService walletService;


  @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ , rollbackFor = Exception.class)
  public OrderVO createOrder(String walletId , BigDecimal price ,
      PaymentMethodEnum paymentMethodEnum, OrderTypeEnums orderTypeEnums)
      throws InsufficientBalanceException, IncreaseBalanceException, OrderTypeException, CreateOrderException, UserNotHasWalletException {

    WalletsDAO walletsDao = walletService.getWalletByWalletId(walletId);
    //TODO 訂單支付時需要負數? walletsDao null 尚未處理 紀錄
    if(walletsDao == null){
      throw new UserNotHasWalletException();
    }

    String userId = walletsDao.getUserId();
    BigDecimal balance = walletsDao.getBalance();

    if (orderTypeEnums == OrderTypeEnums.REDUCE && !walletService.checkReduceBalanceEnough(balance,price)) {
      log.error("InsufficientBalanceException");
      throw new InsufficientBalanceException();
    }

    switch (orderTypeEnums) {
      case INCREASE:
        walletService.increaseBalanceByWalletId(walletId,price);
        break;
      case REDUCE:
        walletService.reduceBalanceByWalletId(walletId,price);
        break;
      default:
        throw new OrderTypeException();
    }
    OrderDAO dao = OrderDAO
        .builder()
        .id(idGeneratorService.getNextId())
        .userId(userId)
        .walletId(walletId)
        .price(price)
        .paymentMethod(paymentMethodEnum.code)
        .type(orderTypeEnums.code)
        .status(1)
        .updateTime(new Date())
        .createTime(new Date())
        .build();
    int i = orderMapper.insertOrder(dao);

    if( i == 0){
      log.error("CreateOrderException");
      throw new CreateOrderException();
    }
    OrderVO vo = OrderVO.builder().build();

    BeanUtils.copyProperties(dao,vo);

    return vo;

  }







//  //測試
//  @Resource
//  @LoadBalanced
//  private RestTemplate restTemplate;
//  public OrderVO getById(String id)
//      throws NotOrderUserException, NoSuchFieldException, IllegalAccessException {
//    OrderDAO order = orderMapper.selectById(id);
//    if(order == null){
//      throw new NotOrderUserException();
//    }
//    String userId = order.getUserId();
//
//    //使用user-service(服務名 讓eureka決定分配到哪台) 需要到Application配置附載均衡 @LoadBalanced
//    String url = "http://user-service/user/user/" + userId;
//
//    System.out.println(url);
//
//    BaseResp baseResp = restTemplate.getForObject(url, BaseResp.class);
//
//    Field field = baseResp.getClass().getDeclaredField("data");
//    field.setAccessible(true);
//    Object dataObj = field.get(baseResp);
//
//    String s = JSON.toJSONString(dataObj);
//    UserVO userVO = JSON.parseObject(s, UserVO.class);
//    OrderVO orderVO = OrderVO
//        .builder()
//        .id(order.getId())
//        .userId(order.getUserId())
//        .walletId(order.getWalletId())
//        .price(order.getPrice())
//        .paymentMethod(order.getPaymentMethod())
//        .type(order.getOrderType())
//        .status(order.getStatus())
//        .price(order.getPrice())
//        .userVO(userVO)
//        .createTime(order.getCreateTime())
//        .updateTime(order.getUpdateTime())
//        .build();
//
//
//    return orderVO;
//  }



}
