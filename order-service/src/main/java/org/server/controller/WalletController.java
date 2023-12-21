package org.server.controller;


import java.math.BigDecimal;
import javax.annotation.Resource;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.server.common.BaseResp;
import org.server.common.StatusCode;
import org.server.controller.rep.wallet.IncreaseBalanceRep;
import org.server.controller.rep.wallet.InitUserWalletRep;
import org.server.controller.rep.wallet.ReduceBalanceRep;
import org.server.dao.WalletsDAO;
import org.server.exception.wallet.AddUserWalletFailException;
import org.server.exception.wallet.IncreaseBalanceException;
import org.server.exception.MissingParameterErrorException;
import org.server.exception.wallet.UserNotHasWalletException;
import org.server.sercice.IdGeneratorService;
import org.server.service.WalletService;
import org.server.vo.WalletsVO;
import org.springframework.beans.BeanUtils;
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
  public BaseResp<InitUserWalletRep> initUserWallet(String userID)
      throws AddUserWalletFailException, MissingParameterErrorException {

    if(StringUtils.isBlank(userID)){
      throw new MissingParameterErrorException();
    }
    WalletsVO vo = walletService.initUserWallet(userID);
    InitUserWalletRep rep = InitUserWalletRep
        .builder()
        .walletsVO(vo)
        .build();
    return BaseResp.ok(rep,StatusCode.Success);

  }

  @PostMapping("/increaseBalance")
  public BaseResp<IncreaseBalanceRep> increaseBalance(String userID, BigDecimal increase)
      throws IncreaseBalanceException, MissingParameterErrorException, UserNotHasWalletException {

    if(StringUtils.isBlank(userID)){
      throw new MissingParameterErrorException();
    }
    if(increase == null){
      throw new MissingParameterErrorException();
    }
    WalletsDAO dao = walletService.getWalletByUserId(userID);

    if(dao == null){
      throw new UserNotHasWalletException();
    }
    String walletId = dao.getWalletId();
    WalletsVO vo = walletService.increaseBalanceByWalletId(walletId,increase);

    IncreaseBalanceRep rep = IncreaseBalanceRep
        .builder()
        .walletsVO(vo)
        .build();
    return BaseResp.ok(rep,StatusCode.Success);

  }

  @PostMapping("/reduceBalance")
  public BaseResp<ReduceBalanceRep> reduceBalance(String userID, BigDecimal reduce)
      throws IncreaseBalanceException, MissingParameterErrorException, UserNotHasWalletException {

    if(StringUtils.isBlank(userID)){
      throw new MissingParameterErrorException();
    }
    if(reduce == null){
      throw new MissingParameterErrorException();
    }
    WalletsDAO dao = walletService.getWalletByUserId(userID);

    if(dao == null){
      throw new UserNotHasWalletException();
    }

    WalletsVO vo = WalletsVO.builder().build();
    ReduceBalanceRep rep = ReduceBalanceRep.builder().build();

    BigDecimal balance = dao.getBalance();
    if (!walletService.checkReduceBalanceEnough(balance, reduce)) {
      BeanUtils.copyProperties(dao,vo);
      rep.setWalletsVO(vo);
      return BaseResp.fail(rep,StatusCode.InsufficientBalance);
    }

    String walletId = dao.getWalletId();
    vo = walletService.reduceBalanceByWalletId(walletId,reduce);
    rep.setWalletsVO(vo);
    return BaseResp.ok(rep,StatusCode.Success);

  }





}


