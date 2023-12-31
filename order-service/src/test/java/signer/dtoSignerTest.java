package signer;


import base.BaseTest;
import java.math.BigDecimal;
import org.junit.Test;
import org.server.withdraw.req.CreateWithdrawRequest;

public class dtoSignerTest extends BaseTest {

  @Test
  public void test() {

    CreateWithdrawRequest req = new CreateWithdrawRequest();

    req.setUserId("setUserId");
    req.setOrderNo("setOrderNo");
    req.setAmount(new BigDecimal(33.21));
    req.setCurrency("setCurrency");
    req.setClientExtra("setClientExtra");
    req.setPayeeCardNo("setPayeeCardNo");
    req.setPayeeCardName("setPayeeCardName");
    req.setBankName("setBankName");
    req.setBranchName("setBranchName");
    req.setBankProvince("setBankProvince");
    req.setBankCity("setBankCity");
    req.setNotifyUrl("setNotifyUrl");

    String mySigh = "mySigh";

    try {
//      //簽名前
      System.out.println("前名前:" + "\t" +req.getSign());
//
//      // 簽署數據
      req.sign(mySigh);
      System.out.println("前名後:" + "\t" +req.getSign());

      // 驗證簽名 對的公鑰(需要執行req.sign)
      boolean isValid = req.verify(mySigh);
      System.out.println("Signature is valid: " + isValid);

      // 驗證簽名 錯的公鑰(需要執行req.sign)
      String mySigh2 = "mySigh2";
      boolean isValid2 = req.verify(mySigh2);
      System.out.println("Signature is valid 2 : " + isValid2);
      
      // 將對象轉換為查詢字符串 生成url
      String queryString = req.toQueryString("https://example.com/api");
      System.out.println("Query String: " + queryString);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }



}
