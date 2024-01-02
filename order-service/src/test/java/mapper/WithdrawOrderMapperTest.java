package mapper;

import base.BaseTest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.junit.Test;
import org.server.mapper.WithdrawOrderMapper;
import org.server.withdraw.model.WithdrawOrder;

public class WithdrawOrderMapperTest extends BaseTest {

  @Resource
  WithdrawOrderMapper withdrawOrderMapper;

  @Test
  public void insertWithdrawOrderTest(){
    WithdrawOrder build = WithdrawOrder.builder()
        .withdrawOrderId("123456") // Replace with an actual withdraw order ID
        .userId("user123") // Replace with an actual user ID
        .bankOrderNo("BANK123") // Replace with an actual bank order number
        .bankReturnCode("SUCCESS") // Replace with an actual bank return code
        .bankReturnMessage("Transaction successful") // Replace with an actual bank return message
        .remark("Withdrawal request") // Replace with an actual remark
        .currency("USD") // Replace with an actual currency code
        .status(0) // Replace with an actual status code
        .payeeCardNo("1234567890123456") // Replace with an actual payee card number
        .bankName("Bank of Example") // Replace with an actual bank name
        .branchName("Example Branch") // Replace with an actual branch name
        .payeeCardName("John Doe") // Replace with an actual payee card name
        .bankProvince("Example Province") // Replace with an actual bank province
        .bankCity("Example City") // Replace with an actual bank city
        .bankCode("123456") // Replace with an actual bank code
        .amount(new BigDecimal("1000.50")) // Replace with an actual withdrawal amount
        .actualAmount(new BigDecimal("1000.00")) // Replace with an actual actual amount
        .rate(0.5) // Replace with an actual rate
        .rateFixedAmount(new BigDecimal("1.50")) // Replace with an actual rate fixed amount
        .notifyUrl("http://example.com/notify") // Replace with an actual notify URL
        .successTime(new Date()) // Replace with an actual success time
        .clientIp("127.0.0.1") // Replace with an actual client IP
        .clientDevice("Mobile") // Replace with an actual client device
        .clientExtra("{'key':'value'}") // Replace with an actual client extra information
        .createTime(new Date()) // Replace with an actual create time
        .updateTime(new Date()) // Replace with an actual update time
        .build();

    int i = withdrawOrderMapper.insertWithdrawOrder(build);
    System.out.println(i);

  }

  @Test
  public void findVerifyPayeeCardNoCountTest(){

    WithdrawOrder query = new WithdrawOrder();
    query.setUserId("user123");
    query.setPayeeCardNo("1234567890123456");
    query.setWithdrawMinute(1000);
    Integer verifyPayeeCardNoCount = withdrawOrderMapper.findVerifyPayeeCardNoCount(query);
    System.out.println(verifyPayeeCardNoCount);
  }

  @Test
  public void test(){
    List<WithdrawOrder> withdrawOrders = withdrawOrderMapper.selectAll();
    System.out.println(withdrawOrders);
  }

}
