package mapper;

import base.BaseTest;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.junit.Test;
import org.server.mapper.WithdrawBankCardBlackMapper;
import org.server.withdraw.model.WithdrawBankCardBlack;

public class WithdrawBankCardBlackMapperTest extends BaseTest {

  @Resource
  private WithdrawBankCardBlackMapper withdrawBankCardBlackMapper;

  @Test
  public void insertWithdrawBankCardBlackTest(){

    WithdrawBankCardBlack build = WithdrawBankCardBlack
        .builder()
        .id("778")
        .merchantId("merchantId123")
        .bankCardNo("bankCardNo123")
        .memo("yourMemo")
        .createIp("yourCreateIp")
        .updateIp("yourUpdateIp")
        .creator("yourCreator")
        .updater("yourUpdater")
        .createTime(new Date())
        .updateTime(new Date())
        .build();
    int i = withdrawBankCardBlackMapper.insertWithdrawBankCardBlack(build);
    System.out.println(i);

  }

  @Test
  public void Test(){

    String merchantId = "merchantId123";
    String bankCardNo = "bankCardNo123";

    List<WithdrawBankCardBlack> byMerchantIdBankCardNo = withdrawBankCardBlackMapper
        .findByMerchantIdBankCardNo(merchantId,bankCardNo);

    for (WithdrawBankCardBlack withdrawBankCardBlack : byMerchantIdBankCardNo) {
      System.out.println(withdrawBankCardBlack);

    }


  }

}
