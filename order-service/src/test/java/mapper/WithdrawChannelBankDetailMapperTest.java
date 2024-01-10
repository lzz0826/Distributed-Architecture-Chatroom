package mapper;

import base.BaseTest;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.junit.Test;
import org.server.mapper.WithdrawChannelBankDetailMapper;
import org.server.withdraw.model.WithdrawChannelBankDetail;

public class WithdrawChannelBankDetailMapperTest extends BaseTest {


  @Resource
  private WithdrawChannelBankDetailMapper withdrawChannelBankDetailMapper;

  @Test
  public void insertWithdrawChannelBankTest(){

    WithdrawChannelBankDetail build = WithdrawChannelBankDetail
        .builder()
        .withdrawChannelBankDetailId("88812")
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

    int i = withdrawChannelBankDetailMapper.insertWithdrawChannelBankDetail(build);

    System.out.println(i);
  }


  @Test
  public void findWithdrawChannelBankByIdTest(){

    String withdrawChannelBankDetailId = "88812";
    WithdrawChannelBankDetail withdrawChannelBankDetailById = withdrawChannelBankDetailMapper.findWithdrawChannelBankDetailById(withdrawChannelBankDetailId);

    System.out.println(withdrawChannelBankDetailById);
  }

  @Test
  public void findByWithdrawBankChannelIdTest(){

    String withdrawBankChannelId = "0204";
    List<WithdrawChannelBankDetail> withdrawChannelBankDetails = withdrawChannelBankDetailMapper.findByWithdrawBankChannelId(withdrawBankChannelId);
    for (WithdrawChannelBankDetail withdrawChannelBankDetail : withdrawChannelBankDetails) {

      System.out.println(withdrawChannelBankDetail);
    }
  }



}
