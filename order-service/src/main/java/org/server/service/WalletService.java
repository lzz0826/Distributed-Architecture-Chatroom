package org.server.service;

import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Resource;
import org.server.dao.WalletsDAO;
import org.server.enums.OrderTypeEnums;
import org.server.exception.order.OrderTypeException;
import org.server.exception.wallet.AddUserWalletFailException;
import org.server.exception.wallet.IncreaseBalanceException;
import org.server.exception.MissingParameterErrorException;
import org.server.exception.wallet.ReduceBalanceException;
import org.server.exception.wallet.UserNotHasWalletException;
import org.server.mapper.WalletsMapper;
import org.server.sercice.IdGeneratorService;
import org.server.vo.WalletsVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WalletService {

  @Resource
  private WalletsMapper walletsMapper;

  @Resource
  private IdGeneratorService idGeneratorService;


  @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ , rollbackFor = Exception.class)
  public WalletsVO initUserWallet(String userId) throws AddUserWalletFailException {
    WalletsVO vo = new WalletsVO();

    System.out.println(userId);

    WalletsDAO repDao = getWalletByUserId(userId);
    System.out.println(repDao);

    if(repDao != null){
      BeanUtils.copyProperties(repDao,vo);
      return vo;
    }
    WalletsDAO dao = WalletsDAO
        .builder()
        .walletId(idGeneratorService.getNextId())
        .userId(userId)
        .balance(BigDecimal.ZERO)
        .status(1)
        .updateTime(new Date())
        .createTime(new Date())
        .build();

    int i = walletsMapper.insertWallets(dao);

    if(i == 0){
      throw new AddUserWalletFailException();
    }
    BeanUtils.copyProperties(dao,vo);
    return vo;

  }

  @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ , rollbackFor = Exception.class)
  public WalletsVO increaseBalanceByWalletId(String walletId , BigDecimal increase)
      throws IncreaseBalanceException {
    WalletsVO vo = new WalletsVO();

    int i = walletsMapper.increaseBalanceByWalletId(walletId, increase , new Date());


    if(i == 0){
      throw  new IncreaseBalanceException();
    }
    WalletsDAO dao = getWalletByWalletId(walletId);

    BeanUtils.copyProperties(dao,vo);
    return vo;

  }

  @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ , rollbackFor = Exception.class)
  public WalletsVO reduceBalanceByWalletId(String walletId , BigDecimal reduce)
      throws  ReduceBalanceException {
    WalletsVO vo = new WalletsVO();
    int i = walletsMapper.reduceBalanceByWalletId(walletId, reduce , new Date());
    if(i == 0){
      throw  new ReduceBalanceException();
    }
    WalletsDAO dao = getWalletByWalletId(walletId);

    BeanUtils.copyProperties(dao,vo);
    return vo;

  }

  @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ , rollbackFor = Exception.class)
  public WalletsVO increaseOrReduceBalance(OrderTypeEnums orderTypeEnums,String walletId,BigDecimal price)
      throws IncreaseBalanceException, OrderTypeException, ReduceBalanceException, UserNotHasWalletException {

    if (!checkHasWallet(walletId)) {
      throw new UserNotHasWalletException();
    }

    WalletsVO vo;
    switch (orderTypeEnums) {
      case INCREASE:
        vo = increaseBalanceByWalletId(walletId, price);
        break;
      case REDUCE:
        vo = reduceBalanceByWalletId(walletId, price);
        break;
      default:
        throw new OrderTypeException();
    }
    return vo;
  }

  public WalletsDAO getWalletByUserId(String userId){
    return walletsMapper.selectByIdUserID(userId);
  }

  public WalletsDAO getWalletByWalletId(String walletId){
    return walletsMapper.selectById(walletId);
  }

  public boolean checkHasWallet(String walletId){
    WalletsDAO dao = getWalletByWalletId(walletId);
    if(dao != null){
      return true;
    }
    return false;
  }

  public boolean checkUserHasWallet(String userId){
    WalletsDAO walletByUserId = getWalletByUserId(userId);
    if(walletByUserId != null){
      return true;
    }
    return false;
  }

  public BigDecimal calculateAddBalance(BigDecimal balance , BigDecimal calculate){
    BigDecimal add = balance.add(calculate);
    return add;
  }

  public boolean checkReduceBalanceEnough(BigDecimal balance, BigDecimal price) {
    BigDecimal lastBalance = balance.subtract(price);
    return lastBalance.compareTo(BigDecimal.ZERO) > 0;
  }



}
