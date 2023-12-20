package org.server.controller;


import com.alibaba.fastjson.JSON;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.server.common.BaseResp;
import org.server.controller.rep.OrderRep;
import org.server.controller.rep.Results;
import org.server.controller.req.OrderReq;
import org.server.exception.AddUserWalletFailException;
import org.server.exception.IncreaseBalanceException;
import org.server.exception.MissingParameterErrorException;
import org.server.exception.NotOrderUserException;
import org.server.service.OrderService;
import org.server.service.WalletService;
import org.server.util.SpringUtil;
import org.server.vo.OrderVO;
import org.server.vo.UserVO;
import org.server.vo.WalletsVO;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wallets")
@Log4j2
public class WalletsController {



  @Resource
  private WalletService walletService;

  @PostMapping("/initUserWallet")
  public BaseResp<WalletsVO> initUserWallet(String userID)
      throws AddUserWalletFailException, MissingParameterErrorException {

    if(StringUtils.isBlank(userID)){
      throw new MissingParameterErrorException();
    }

    WalletsVO vo = walletService.initUserWallet(userID);

    return BaseResp.ok(vo);

  }

  @PostMapping("/increaseBalance")
  public BaseResp<WalletsVO> increaseBalance(String walletId, BigDecimal balance)
      throws  IncreaseBalanceException, MissingParameterErrorException {

    if(StringUtils.isBlank(walletId)){
      throw new MissingParameterErrorException();
    }
    if(balance == null){
      throw new MissingParameterErrorException();
    }

    WalletsVO vo = walletService.increaseBalanceByUserId(walletId,balance);

    return BaseResp.ok(vo);

  }





}


