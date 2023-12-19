package mapper;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.junit.Test;
import org.server.dao.WalletsDAO;
import org.server.mapper.WalletsMapper;
import org.server.sercice.IdGeneratorService;

public class WalletsMapperTest extends BaseTest{

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




}
