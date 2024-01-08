package mapper;

import base.BaseTest;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import jodd.util.CsvUtil;
import org.junit.Test;
import org.server.mapper.WithdrawChannelBankMapper;
import org.server.withdraw.model.WithdrawChannelBank;
import org.server.withdraw.model.WithdrawChannelBank.WithdrawChannelBankBuilder;

public class WithdrawChannelBankMapperTest extends BaseTest {


  @Resource
  private WithdrawChannelBankMapper withdrawChannelBankMapper;

  @Test
  public void insertWithdrawChannelBankTest(){

    WithdrawChannelBank build = WithdrawChannelBank
        .builder()
        .withdrawChannelBankId("88812")
        .withdrawBankChannelId("0204")
        .bankCodeId("9993")
        .bankName("BBK銀行")
        .bankCode("BBK")
        .status(0) // or 1 based on your requirement
        .orderOriginationUrl("yourOrderOriginationUrl")
        .orderQueryUrl("yourOrderQueryUrl")
        .balanceQueryUrl("yourBalanceQueryUrl")
        .updater("yourUpdater")
        .creator("yourCreator")
        .createIp("yourCreateIp")
        .updateIp("yourUpdateIp")
        .ourNotifyUrl("yourOurNotifyUrl")
        .memo("yourMemo")
        .updateTime(new Date()) // or provide an appropriate date
        .createTime(new Date()) // or provide an appropriate date
        .build();

    int i = withdrawChannelBankMapper.insertWithdrawChannelBank(build);

    System.out.println(i);
  }


  @Test
  public void findWithdrawChannelBankByIdTest(){

    String withdrawChannelBankId = "888812";
    WithdrawChannelBank withdrawChannelBankById = withdrawChannelBankMapper.findWithdrawChannelBankById(withdrawChannelBankId);

    System.out.println(withdrawChannelBankId);
  }

  @Test
  public void findByWithdrawBankChannelIdTest(){

    String withdrawBankChannelId = "0204";
    List<WithdrawChannelBank> withdrawChannelBanks = withdrawChannelBankMapper.findByWithdrawBankChannelId(withdrawBankChannelId);
    for (WithdrawChannelBank withdrawChannelBank : withdrawChannelBanks) {

      System.out.println(withdrawChannelBank);
    }
  }


}
