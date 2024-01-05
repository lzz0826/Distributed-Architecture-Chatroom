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
        .merchantId("merchantId123")
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
  public void selectAllTest(){
    List<WithdrawOrder> withdrawOrders = withdrawOrderMapper.selectAll();
    System.out.println(withdrawOrders);
  }

  @Test
  public void withdrawOrderMapperTest(){
    String withdrawOrderId = "w4254456175125623440";
    WithdrawOrder withdrawOrder = withdrawOrderMapper.selectByWithdrawOrderId(withdrawOrderId);
    System.out.println(withdrawOrder);
  }

  @Test
  public void selectByMerchantIdAndBankOrderNoTest(){

    String merchantId = "merchantId123";

    String orderNo = "BANK123";


    List<WithdrawOrder> withdrawOrders =
        withdrawOrderMapper.selectByMerchantIdAndBankOrderNo(merchantId,orderNo);
    if(withdrawOrders.isEmpty()){
      System.out.println("nnn");
    }
    for (WithdrawOrder withdrawOrder : withdrawOrders) {
      System.out.println(withdrawOrder);
    }
  }

  @Test
  public void selectByStatusAndDayTest(){

    List<WithdrawOrder>  withdrawOrder = withdrawOrderMapper.selectByStatusAndDay(9,3);
    System.out.println(withdrawOrder);
  }

  @Test
  public void updateWithdrawOrderStatusTest(){
    WithdrawOrder build = WithdrawOrder
        .builder()
        .withdrawOrderId("w4254456175125623440")
        .status(WithdrawOrder.STATUS_FAILED_ON_MERCHANT_CONFIRM)
        .build();
    int i = withdrawOrderMapper.updateWithdrawOrderStatus(build);
    System.out.println(i);
  }

  @Test
  public void updateWithdrawOrderTest(){
    WithdrawOrder build = WithdrawOrder.builder()
        .withdrawOrderId("w4254398053614971437") // Replace with an actual withdraw order ID
        .merchantId("xxxmerchantId123")
        .userId("xxxuser123") // Replace with an actual user ID
        .bankOrderNo("xxxBANK123") // Replace with an actual bank order number
        .bankReturnCode("xxxSUCCESS") // Replace with an actual bank return code
        .bankReturnMessage("xxxTransaction successful") // Replace with an actual bank return message
        .remark("xxxWithdrawal request") // Replace with an actual remark
        .currency("xxxUSD") // Replace with an actual currency code
        .status(8) // Replace with an actual status code
        .payeeCardNo("xxx1234567890123456") // Replace with an actual payee card number
        .bankName("xxxBank of Example") // Replace with an actual bank name
        .branchName("xxxExample Branch") // Replace with an actual branch name
        .payeeCardName("xxxJohn Doe") // Replace with an actual payee card name
        .bankProvince("xxxExample Province") // Replace with an actual bank province
        .bankCity("xxxExample City") // Replace with an actual bank city
        .bankCode("xxx123456") // Replace with an actual bank code
        .amount(new BigDecimal("8000.50")) // Replace with an actual withdrawal amount
        .actualAmount(new BigDecimal("8000.00")) // Replace with an actual actual amount
        .rate(8.5) // Replace with an actual rate
        .rateFixedAmount(new BigDecimal("2.50")) // Replace with an actual rate fixed amount
        .notifyUrl("http://xxxxexample.com/notify") // Replace with an actual notify URL
        .successTime(new Date()) // Replace with an actual success time
        .clientIp("827.0.0.1") // Replace with an actual client IP
        .clientDevice("xxxMobile") // Replace with an actual client device
        .clientExtra("{'key':'xxxxvalue'}") // Replace with an actual client extra information
        .createTime(new Date()) // Replace with an actual create time
        .updateTime(new Date()) // Replace with an actual update time
        .build();

    int i = withdrawOrderMapper.updateWithdrawOrder(build);

    System.out.println(i);


  }



}
