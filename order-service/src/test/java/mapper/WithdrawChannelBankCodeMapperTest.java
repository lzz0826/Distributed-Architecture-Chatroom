package mapper;

import base.BaseTest;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.junit.Test;
import org.server.mapper.WithdrawChannelBankCodeMapper;
import org.server.withdraw.model.WithdrawChannelBankCode;

public class WithdrawChannelBankCodeMapperTest extends BaseTest {

  @Resource
  private WithdrawChannelBankCodeMapper withdrawChannelBankCodeMapper;


  @Test
  public void insertWithdrawChannelBankCodeTest(){
    WithdrawChannelBankCode withdrawChannelBankCode = WithdrawChannelBankCode
        .builder()
        .bankCodeId("999")
        .withdrawBankChannelId("1")
        .bankCode("BBK")
        .bankName("testBank")
        .updateTime(new Date())
        .createTime(new Date())
        .build();



    int i = withdrawChannelBankCodeMapper.insertWithdrawChannelBankCode(withdrawChannelBankCode);

    System.out.println(i);

  }

  @Test
  public void findByBankNameTest(){

    String bankName = "testBank";
    List<WithdrawChannelBankCode> byBankName = withdrawChannelBankCodeMapper.findByBankName(bankName);
    System.out.println(byBankName);
  }

}
