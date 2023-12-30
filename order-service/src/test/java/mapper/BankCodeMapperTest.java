package mapper;


import base.BaseTest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.junit.Test;
import org.server.dao.BankCodeDAO;
import org.server.dao.BankcardAccountDAO;
import org.server.mapper.BankAccountMapper;
import org.server.mapper.BankCodeMapper;
import org.server.sercice.IdGeneratorService;

public class BankCodeMapperTest extends BaseTest {

  @Resource
  private BankCodeMapper bankCodeMapper;

  @Resource
  private IdGeneratorService idGeneratorService;


  @Test
  public void insertBankCodeTest(){

    BankCodeDAO dao = BankCodeDAO
        .builder()
        .bankId(idGeneratorService.getNextId())
        .bankCode("bankCode")
        .bankAbbreviationn("BAS")
        .bankName("bankName")
        .updateTime(new Date())
        .createTime(new Date())
        .updater("updater")
        .creator("creator")
        .build();

    int i = bankCodeMapper.insertOrder(dao);

  }

  @Test
  public void selectAllTest(){
    List<BankCodeDAO> bankCodeDAOS = bankCodeMapper.selectAll();
    for (BankCodeDAO bankCodeDAO : bankCodeDAOS) {
      System.out.println(bankCodeDAO);
    }
  }

  @Test
  public void selectByIdTest(){

    BankCodeDAO dao = bankCodeMapper.selectById("4223484276762313952");
    System.out.println(dao);

  }

  @Test
  public void updateByIdTest(){

    BankCodeDAO dao = BankCodeDAO
        .builder()
        .bankId("4223484276762313952")
        .bankCode("bankCode1")
        .bankAbbreviationn("BAS1")
        .bankName("bankName1")
        .updateTime(new Date())
        .createTime(new Date())
        .updater("updater1")
        .creator("creator1")
        .build();

    int i = bankCodeMapper.updateById(dao);


  }





}
