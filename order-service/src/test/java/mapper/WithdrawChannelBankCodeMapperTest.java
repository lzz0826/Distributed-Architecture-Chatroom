package mapper;

import base.BaseTest;
import java.util.ArrayList;
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
        .bankCodeId("9993")
        .bankCode("BBK")
        .bankName("testBank")
        .status(1)
        .memo("memo")
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

  @Test
  public void findByBankCodeTest(){

    String bankCode = "BBK";

    List<WithdrawChannelBankCode> byBankCode = withdrawChannelBankCodeMapper.findByBankCode(bankCode);
    for (WithdrawChannelBankCode withdrawChannelBankCode : byBankCode) {
      System.out.println(withdrawChannelBankCode);
    }
  }

  @Test
  public void findByIBankCodeIdsTest(){
    List<String> s = new ArrayList<>();
    s.add("9993");
    s.add("");
    List<WithdrawChannelBankCode> byIBankCodeIds = withdrawChannelBankCodeMapper.findByIBankCodeIds(s);
    System.out.println(byIBankCodeIds);
  }

}
