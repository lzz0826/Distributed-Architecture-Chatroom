package mapper;

import base.BaseTest;
import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Resource;
import org.junit.Test;
import org.server.mapper.MerchantMapper;
import org.server.withdraw.model.Merchant;
import org.server.withdraw.model.Merchant.MerchantBuilder;

public class MerchantMapperTest extends BaseTest {

  @Resource
  private MerchantMapper merchantMapper;

  @Test
  public void insertMerchantTest(){

    Merchant merchant = Merchant.builder()
        .merchantId("merchantId123")
        .merchantName("yourMerchantName123")
        .userId("userId123")
        .requestKey("yourRequestKey")
        .publicKey("yourPublicKey")
        .privateKey("yourPrivateKey")
        .balance(BigDecimal.valueOf(1000.0)) // Set the balance value
        .frozenAmount(BigDecimal.valueOf(500.0)) // Set the frozenAmount value
        .bankName("yourBankName")
        .payeeCardName("yourPayeeCardName")
        .payeeCardNo("yourPayeeCardNo")
        .status(1) // Set the status value
        .memo("yourMemo")
        .mobile("yourMobile")
        .email("yourEmail")
        .createTime(new Date())
        .updateTime(new Date())
        .creator("yourCreator")
        .updater("yourUpdater")
        .isBackendLogin(1) // Set the isBackendLogin value
        .password("yourPassword")
        .sort("yourSort")
        .build();

    int i = merchantMapper.insertMerchant(merchant);
    System.out.println(i);

  }

  @Test
  public void selectByUserIdTest(){


    Merchant merchant = merchantMapper.selectByUserId("yourUserId");
    System.out.println(merchant);
  }

}
