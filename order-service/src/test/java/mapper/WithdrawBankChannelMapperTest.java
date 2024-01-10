package mapper;

import base.BaseTest;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import javax.annotation.Resource;
import org.junit.Test;
import org.server.withdraw.model.WithdrawChannel;
import org.server.mapper.WithdrawBankChannelMapper;

public class WithdrawBankChannelMapperTest extends BaseTest {


  @Resource
  private WithdrawBankChannelMapper withdrawBankChannelMapper;

  @Test
  public void insertWithdrawBankChannelTest(){
    WithdrawChannel dao = WithdrawChannel.builder()
        .withdrawBankChannelId("0204L")
        .withdrawBankChannelName("Channel Name")
        .withdrawBankChannelCode("BBK")
        .merchantId("merchantId123")
        .userId("userId123")
        .status(1)
        .bankChannelMerchantId("Merchant ID")
        .bankChannelMerchantName("Merchant Name")
        .bankChannelExtra("bankChannelExtra") // Assuming it's a BLOB and setting it to null for this example
        .publicKey("publicKey") // Assuming it's a BLOB and setting it to null for this example
        .privateKey("privateKey") // Assuming it's a BLOB and setting it to null for this example
        .loginUsername("Login User")
        .loginPassword("Login Password")
        .minAmount(600L)
        .maxAmount(6000L)
        .dayMaxAmount(5000L)
        .dayMaxCount(10L)
        .totalAmount(100000L)
        .totalCount(1000L)
        .todayAmount(5000L)
        .todayCount(5L)
        .memo("Test Memo")
        .costRate(0.05)
        .costFixedAmount(500L)
        .balance(new BigDecimal(5000))
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
    WithdrawChannel withdrawChannelById = withdrawBankChannelMapper.getWithdrawChannelDaoById("2");
    System.out.println(withdrawChannelById);
  }

  @Test
  public void getWithdrawChannelDaoByUserIdTest(){
    WithdrawChannel withdrawChannelById = withdrawBankChannelMapper.getWithdrawChannelDaoByUserId("123456");
    System.out.println(withdrawChannelById);
  }



  @Test
  public void getWithdrawChannelDaoByMerchantIdTest(){
    WithdrawChannel withdrawChannelById = withdrawBankChannelMapper.getWithdrawChannelDaoByMerchantId("merchantId123");
    System.out.println(withdrawChannelById);
  }

  @Test
  public void findByMerchantIdsTest(){

    String merchantId = "merchantId123";

    List<WithdrawChannel> withdrawChannels = withdrawBankChannelMapper.findByMerchantIds(merchantId);
    System.out.println(withdrawChannels);
  }

  @Test
  public void findByMerchantIdAndAndAmountTest(){
    String merchantId = "merchantId123";
    BigDecimal amount = new BigDecimal(5100);

    List<WithdrawChannel> byMerchantIdAndAndAmount = withdrawBankChannelMapper.findByMerchantIdAndAndAmount(merchantId,amount);
    for (WithdrawChannel withdrawChannel : byMerchantIdAndAndAmount) {
      System.out.println(withdrawChannel);
    }
  }


}
