package org.server.controller;


import java.math.BigDecimal;
import javax.annotation.Resource;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.server.common.BaseResp;
import org.server.dao.WalletsDAO;
import org.server.exception.wallet.AddUserWalletFailException;
import org.server.exception.wallet.IncreaseBalanceException;
import org.server.exception.MissingParameterErrorException;
import org.server.exception.wallet.UserNotHasWalletException;
import org.server.service.WalletService;
import org.server.vo.WalletsVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wallets")
@Log4j2
public class WalletController {



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
  public BaseResp<WalletsVO> increaseBalance(String userID, BigDecimal balance)
      throws IncreaseBalanceException, MissingParameterErrorException, UserNotHasWalletException {

    if(StringUtils.isBlank(userID)){
      throw new MissingParameterErrorException();
    }
    if(balance == null){
      throw new MissingParameterErrorException();
    }
    WalletsDAO dao = walletService.getWalletByUserId(userID);

    if(dao == null){
      throw new UserNotHasWalletException();
    }
    String walletId = dao.getWalletId();
    WalletsVO vo = walletService.increaseBalanceByWalletId(walletId,balance);
    return BaseResp.ok(vo);

  }





}


