package org.server.service;

import com.alibaba.fastjson2.JSON;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.server.common.BaseResp;
import org.server.dao.OrderDAO;
import org.server.dao.WalletsDAO;
import org.server.exception.AddUserWalletFailException;
import org.server.exception.NotOrderUserException;
import org.server.mapper.OrderMapper;
import org.server.mapper.WalletsMapper;
import org.server.sercice.IdGeneratorService;
import org.server.vo.OrderVO;
import org.server.vo.UserVO;
import org.server.vo.WalletsVO;
import org.springframework.beans.BeanUtils;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

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
    System.out.println("1111");

    System.out.println(repDao);


    if(repDao != null){
      System.out.println("5555");
      BeanUtils.copyProperties(repDao,vo);
      return vo;
    }
    System.out.println("666");

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

    if(i < 1){
      throw new AddUserWalletFailException();
    }
    BeanUtils.copyProperties(dao,vo);
    return vo;

  }

  public WalletsDAO getWalletByUserId(String userId){
    return walletsMapper.selectByIdUserID(userId);
  }

  public boolean checkUserHasWallet(String userId){
    WalletsDAO walletByUserId = getWalletByUserId(userId);
    if(walletByUserId != null){
      return true;
    }
    return false;
  }



}
