package mapper;

import base.BaseTest;
import java.math.BigDecimal;
import java.sql.Timestamp;
import javax.annotation.Resource;
import org.junit.Test;
import org.server.dao.WithdrawChannelDao;
import org.server.mapper.WithdrawBankChannelMapper;

public class WithdrawBankChannelMapperTest extends BaseTest {


  @Resource
  private WithdrawBankChannelMapper withdrawBankChannelMapper;

  @Test
  public void insertWithdrawBankChannelTest(){
    WithdrawChannelDao dao = WithdrawChannelDao.builder()
        .withdrawBankChannelId(1L)
        .withdrawBankChannelName("Channel Name")
        .withdrawBankChannelCode("Channel Code")
        .userId("123456")
        .status(1)
        .bankChannelMerchantId("Merchant ID")
        .bankChannelMerchantName("Merchant Name")
        .bankChannelExtra("bankChannelExtra") // Assuming it's a BLOB and setting it to null for this example
        .publicKey("publicKey") // Assuming it's a BLOB and setting it to null for this example
        .privateKey("privateKey") // Assuming it's a BLOB and setting it to null for this example
        .loginUsername("Login User")
        .loginPassword("Login Password")
        .minAmount(100L)
        .maxAmount(1000L)
        .dayMaxAmount(5000L)
        .dayMaxCount(10L)
        .totalAmount(100000L)
        .totalCount(1000L)
        .todayAmount(5000L)
        .todayCount(5L)
        .memo("Test Memo")
        .costRate(0.05)
        .costFixedAmount(500L)
        .balance(new BigDecimal(222.3))
        .notifyUrl("http://example.com/callback")
        .bankAreaCode(123)
        .bankCode("ABC123")
        .openStartTime(Timestamp.valueOf("2022-01-01 08:00:00"))
        .openEndTime(Timestamp.valueOf("2022-01-01 18:00:00"))
        .createTime(Timestamp.valueOf("2022-01-01 12:00:00"))
        .updateTime(Timestamp.valueOf("2022-01-01 14:00:00"))
        .creator("John")
        .updater("Doe")
        .build();


    int i = withdrawBankChannelMapper.insertWithdrawBankChannel(dao);
    System.out.println(i);

  }

  @Test
  public void getWithdrawChannelDaoByIdTest(){
    WithdrawChannelDao withdrawChannelDaoById = withdrawBankChannelMapper.getWithdrawChannelDaoById("1");
    System.out.println(withdrawChannelDaoById);
  }

  @Test
  public void getWithdrawChannelDaoByUserIdTest(){
    WithdrawChannelDao withdrawChannelDaoById = withdrawBankChannelMapper.getWithdrawChannelDaoByUserId("123456");
    System.out.println(withdrawChannelDaoById);
  }



}
