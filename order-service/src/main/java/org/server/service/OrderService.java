package org.server.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import lombok.extern.log4j.Log4j2;
import org.server.dao.WalletsDAO;
import org.server.enums.OrderLockEnums;
import org.server.enums.OrderStatusEnums;
import org.server.enums.OrderTypeEnums;
import org.server.enums.PaymentMethodEnum;
import org.server.exception.order.CallBackProcessingException;
import org.server.exception.order.CreateOrderException;
import org.server.exception.order.NotFoundOderIdException;
import org.server.exception.order.OrderStatusException;
import org.server.exception.order.OrderTypeException;
import org.server.exception.wallet.IncreaseBalanceException;
import org.server.exception.wallet.InsufficientBalanceException;
import org.server.exception.wallet.ReduceBalanceException;
import org.server.exception.wallet.UserNotHasWalletException;
import org.server.mapper.OrderMapper;
import org.server.dao.OrderDAO;
import org.server.sercice.IdGeneratorService;
import org.server.util.DistributedLock;
import org.server.vo.CallBackOrderVO;
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

  @Resource
  private AsyncService asyncService;

  @Resource
  private DistributedLock distributedLock;


  /**
   * 創建定單(只創建 不修改錢包)
   */
  @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ , rollbackFor = Exception.class)
  public OrderVO createOrder(String walletId , BigDecimal price ,
      PaymentMethodEnum paymentMethodEnum, OrderTypeEnums orderTypeEnums)
      throws InsufficientBalanceException,  CreateOrderException, UserNotHasWalletException {

    WalletsDAO walletsDao = walletService.getWalletByWalletId(walletId);
    //TODO 訂單支付時需要負數?
    if(walletsDao == null){
      throw new UserNotHasWalletException();
    }

    String userId = walletsDao.getUserId();
    BigDecimal balance = walletsDao.getBalance();

    if (orderTypeEnums == OrderTypeEnums.REDUCE && !walletService.checkReduceBalanceEnough(balance,price)) {
      log.error("InsufficientBalanceException");
      throw new InsufficientBalanceException();
    }

    String orderId = idGeneratorService.getNextId();
    //添加訂單 打其他服務 確認等回調後再對本地錢包進行操作
    asyncService.callOtherServer(orderId,userId,price);

    OrderDAO dao = OrderDAO
        .builder()
        .id(orderId)
        .userId(userId)
        .walletId(walletId)
        .price(price)
        .paymentMethod(paymentMethodEnum.code)
        .type(orderTypeEnums.code)
        .status(OrderStatusEnums.CREATE.code)
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

  /**
   * 本地錢包轉帳 (包含更新錢包)
   */
  @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ , rollbackFor = Exception.class)
  public OrderVO localTransferOrder(String userId,String walletId,String targetUserId,String targetWalletId,
      BigDecimal price,PaymentMethodEnum paymentMethodEnum)
      throws UserNotHasWalletException, InsufficientBalanceException, IncreaseBalanceException, CreateOrderException, ReduceBalanceException {

    WalletsDAO walletsDao = walletService.getWalletByWalletId(walletId);
    WalletsDAO targetWalletsDao = walletService.getWalletByWalletId(targetWalletId);

    if(walletsDao == null || targetWalletsDao == null){
      throw new UserNotHasWalletException();
    }
    BigDecimal balance = walletsDao.getBalance();

    if (!walletService.checkReduceBalanceEnough(balance,price)) {
      throw new InsufficientBalanceException();
    }
    walletService.reduceBalanceByWalletId(walletId,price);
    walletService.increaseBalanceByWalletId(targetWalletId,price);

    OrderDAO dao = OrderDAO
        .builder()
        .id(idGeneratorService.getNextId())
        .userId(userId)
        .walletId(walletId)
        .targetUserId(targetUserId)
        .targetWalletId(targetWalletId)
        .price(price)
        .paymentMethod(paymentMethodEnum.code)
        .type(OrderTypeEnums.LOCAL_TRANSFER.code)
        .status(OrderStatusEnums.SUCCESS.code)
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

  public OrderDAO getOrderById(String orderId){
    return orderMapper.selectById(orderId);
  }


  /**
   * 訂單回調 (以訂單號作為分布式鎖)
   */
  public CallBackOrderVO  v(String orderId , BigDecimal price,OrderStatusEnums orderStatusEnums)
      throws CallBackProcessingException, ReduceBalanceException, IncreaseBalanceException, OrderStatusException, UserNotHasWalletException, OrderTypeException, NotFoundOderIdException {
    if(distributedLock.isLock(OrderLockEnums.CALLBACK.name + orderId)){
      log.info("訂單號={}, 時間段内重複回調",orderId);
      throw new CallBackProcessingException();
    }
    try {
      distributedLock.lock(OrderLockEnums.CALLBACK.name + orderId);
      return jugeAndUpdateOrder(orderId,price,orderStatusEnums);
    }finally {
      distributedLock.unlock(OrderLockEnums.CALLBACK.name + orderId);
    }
  }


  /**
   * 判斷訂單狀態 (包含更新錢包)
   */
  @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ , rollbackFor = Exception.class)
  public CallBackOrderVO jugeAndUpdateOrder(String orderId , BigDecimal price,OrderStatusEnums orderStatusEnums)
      throws IncreaseBalanceException, OrderTypeException, NotFoundOderIdException, OrderStatusException, ReduceBalanceException, UserNotHasWalletException {

    OrderDAO dao = getOrderById(orderId);
    OrderTypeEnums orderTypeEnums = OrderTypeEnums.parse(dao.getType());
    String walletId = dao.getWalletId();


    switch (orderStatusEnums) {
      case CREATE:
        updateOrderStatue(orderId , orderStatusEnums);
        break;
      case PAYING:
        updateOrderStatue(orderId , orderStatusEnums);
        break;
      case SUCCESS:
        walletService.increaseOrReduceBalance(orderTypeEnums, walletId, price);
        updateOrderStatue(orderId , orderStatusEnums);
        break;
      case FAIL:
        updateOrderStatue(orderId , orderStatusEnums);
        break;
      default:
        throw new OrderStatusException();
    }

    OrderDAO orderDAO = getOrderById(orderId);

    CallBackOrderVO vo = CallBackOrderVO
        .builder()
        .orderId(orderDAO.getId())
        .price(orderDAO.getPrice())
        .type(orderDAO.getType())
        .status(orderDAO.getStatus())
        .updateTime(orderDAO.getUpdateTime())
        .build();

    return vo;
  }



  /**
   * 更新訂單狀態
   */
  @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ , rollbackFor = Exception.class)
  public void updateOrderStatue(String orderId , OrderStatusEnums orderStatusEnums)
      throws NotFoundOderIdException {
    OrderDAO dao = OrderDAO
        .builder()
        .id(orderId)
        .status(orderStatusEnums.code)
        .updateTime(new Date())
        .build();
    int i = orderMapper.updateByOrderId(dao);
    if(i == 0){
      throw new NotFoundOderIdException();
    }

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
