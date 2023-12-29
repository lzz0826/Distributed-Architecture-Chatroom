package mapper;


import base.BaseTest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.junit.Test;
import org.server.dao.BankcardAccountDAO;
import org.server.dao.WalletsDAO;
import org.server.mapper.BankAccountMapper;
import org.server.sercice.IdGeneratorService;

public class BankAccountMapperTest extends BaseTest {

  @Resource
  private BankAccountMapper bankAccountMapper;

  @Resource
  private IdGeneratorService idGeneratorService;


  @Test
  public void insertBankAccountTest(){

    BankcardAccountDAO dao = BankcardAccountDAO
        .builder()
        .bankCardAccountId(idGeneratorService.getNextId())
        .bankId(idGeneratorService.getNextId())
        .cardId("cardId")
        .type(1)
        .payeeCardName("payeeCardName")
        .payeeCardNo("payeeCardNo")
        .bankName("bankName")
        .bankArea("bankArea")
        .branchName("branchName")
        .status(2)
        .memo("memo")
        .balance(new BigDecimal(2.33))
        .loginUsername("loginUsername")
        .loginPassword("loginPassword")
        .transactionPassword("transactionPassword")
        .mobile("mobile")
        .createTime(new Date())
        .updateTime(new Date())
        .creator("creator")
        .updater("updater")
        .build();
    int i = bankAccountMapper.insertBankAccount(dao);

  }

  @Test
  public void selectAllTest(){

    List<BankcardAccountDAO> bankcardAccountDAOS = bankAccountMapper.selectAll();
    for (BankcardAccountDAO bankcardAccountDAO : bankcardAccountDAOS) {
      System.out.println("----");
      System.out.println(bankcardAccountDAO);
      System.out.println("----");
    }

  }

  @Test
  public void selectByBankCardAccountIdTest(){
    BankcardAccountDAO dao = bankAccountMapper.selectByBankCardAccountId("4219640852598114607");
    System.out.println(dao);

  }


  @Test
  public void updateByBankcardAccountTest(){
    BankcardAccountDAO dao = BankcardAccountDAO
        .builder()
        .bankCardAccountId("4219640852598114607")
        .bankId(idGeneratorService.getNextId())
        .cardId("cardId1111")
        .type(9)
        .payeeCardName("payeeCardName111")
        .payeeCardNo("payeeCardNo111")
        .bankName("bankName111")
        .bankArea("bankArea11")
        .branchName("branchName111")
        .status(9)
        .memo("memo111")
        .balance(new BigDecimal(2111.33))
        .loginUsername("loginUsername111")
        .loginPassword("loginPassword1111")
        .transactionPassword("transactionPassword1111")
        .mobile("mobile111")
        .createTime(new Date())
        .updateTime(new Date())
        .creator("creator111")
        .updater("updater111")
        .build();
    int i = bankAccountMapper.updateByBankcardAccount(dao);
    System.out.println(dao);

  }






}
