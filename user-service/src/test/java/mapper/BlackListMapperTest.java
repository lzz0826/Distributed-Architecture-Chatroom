package mapper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.junit.Test;
import org.server.dao.BlackListDAO;
import org.server.mapper.BlackListMapper;

public class BlackListMapperTest extends BaseTest{

  @Resource
  private BlackListMapper blackListMapper;

  @Test
  public void testSelectByBlacklist(){
    List<BlackListDAO> daos = blackListMapper.selectByBlacklist("33333333");
    System.out.println(daos);
  }


  @Test
  public void testInsertBlackLists(){
    List<BlackListDAO> daos = new ArrayList<>();

    BlackListDAO build = BlackListDAO
        .builder()
        .id("2222")
        .userId("6666")
        .blacklist("88888")
        .updateTime(new Date())
        .createTime(new Date())
        .build();

    BlackListDAO build2 = BlackListDAO
        .builder()
        .id("9999")
        .userId("")
        .updateTime(new Date())
        .createTime(new Date())
        .build();

    daos.add(build);
    daos.add(build2);

    int i = blackListMapper.insertBlackLists(daos);
    System.out.println();

  }


  @Test
  public void deleteByIdsTest(){
    List<String> list = new ArrayList<>();
    list.add("123");
    list.add("789");
    int i = blackListMapper.deleteByIds(list);
    System.out.println(i);

  }


  @Test
  public void deleteByUserIdsTest(){
    List<String> list = new ArrayList<>();
    list.add("456");
    System.out.println();
    int i = blackListMapper.deleteByUserIds(list);
    System.out.println(i);

  }


  @Test
  public void updateListTest(){
    BlackListDAO build = BlackListDAO
        .builder()
        .id("123")
        .userId("6666")
        .blacklist("88888")
        .updateTime(new Date())
        .createTime(new Date())
        .build();

    System.out.println("build : "+build);
    int i = blackListMapper.update(build);
    System.out.println(i);
  }


  @Test
  public void testSelectByUserId(){
    List<BlackListDAO> blackListDAO = blackListMapper.selectByUserId("000001");
    System.out.println(blackListDAO);

  }

  @Test
  public void selectByIdTest(){
    BlackListDAO blackListDAO = blackListMapper.selectById("123");
    System.out.println(blackListDAO);

  }

  @Test
  public void testInsertBlackList (){
    BlackListDAO build = BlackListDAO
        .builder()
        .id("123")
        .userId("456")
        .blacklist("765")
        .updateTime(new Date())
        .createTime(new Date())
        .build();

    int i = blackListMapper.insertBlackList(build);
    System.out.println(i);

  }



}
