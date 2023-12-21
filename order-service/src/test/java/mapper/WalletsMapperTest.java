package mapper;


import base.BaseTest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.junit.Test;
import org.server.dao.WalletsDAO;
import org.server.mapper.WalletsMapper;
import org.server.sercice.IdGeneratorService;

public class WalletsMapperTest extends BaseTest {

  @Resource
  private WalletsMapper walletsMapper;

  @Resource
  private IdGeneratorService idGeneratorService;


  @Test
  public void insertOrderTest(){
    WalletsDAO tony = WalletsDAO
        .builder()
        .walletId(idGeneratorService.getNextId())
        .userId(idGeneratorService.getNextId())
        .balance(new BigDecimal(100.222222))
        .status(2)
        .createTime(new Date())
        .updateTime(new Date())
        .build();
    int i = walletsMapper.insertWallets(tony);
    System.out.println(i);
  }

  @Test
  public void selectAllTest(){

    List<WalletsDAO> walletsDAOS = walletsMapper.selectAll();
    System.out.println(walletsDAOS);
  }

  @Test
  public void selectByIdTest(){

    WalletsDAO walletsDAO = walletsMapper.selectById("456");
    System.out.println(walletsDAO);
  }

  @Test
  public void selectByIdUserIDTest(){
    WalletsDAO walletsDAO = walletsMapper.selectByIdUserID("456");
    if(walletsDAO == null){
      System.out.println("空");
    }
    System.out.println(walletsDAO);
  }


  @Test
  public void updateByUserIdTest(){

    WalletsDAO build = WalletsDAO
        .builder()
        .walletId("33")
        .userId("4159499042618433945")
        .status(2)
        .balance(new BigDecimal(120.44))
        .createTime(new Date())
        .updateTime(new Date())
        .build();

    walletsMapper.updateByUserId(build);

  }

  @Test
  public void increaseBalanceByUserIdTest(){
    String userId = "4159499042618433945";
    BigDecimal bigDecimal = new BigDecimal(44.5);
    walletsMapper.increaseBalanceByWalletId(userId,bigDecimal,new Date());
  }

  @Test
  public void reduceBalanceByUserIdTest(){
    String userId = "4159499042618433945";
    BigDecimal bigDecimal = new BigDecimal(434.5);
    walletsMapper.reduceBalanceByWalletId(userId,bigDecimal,new Date());
  }






}
