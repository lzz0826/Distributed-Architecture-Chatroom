package service;


import base.BaseTest;
import java.math.BigDecimal;
import javax.annotation.Resource;
import org.junit.Test;
import org.server.exception.wallet.IncreaseBalanceException;
import org.server.exception.MissingParameterErrorException;
import org.server.exception.wallet.ReduceBalanceException;
import org.server.sercice.IdGeneratorService;
import org.server.service.WalletService;
import org.server.vo.WalletsVO;

public class WalletServiceTest extends BaseTest {

  @Resource
  private IdGeneratorService idGeneratorService;

  @Resource
  private WalletService walletService;


  @Test
  public void reduceBalanceByWalletIdTest()
      throws IncreaseBalanceException, MissingParameterErrorException, ReduceBalanceException {

    String walletId = "4161812802905689774";
    BigDecimal balance = new BigDecimal(33.55);

    WalletsVO vo = walletService.reduceBalanceByWalletId(walletId,balance);

    System.out.println(vo);

  }



}
