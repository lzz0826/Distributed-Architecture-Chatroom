package mapper;

import java.util.List;
import org.server.mapper.UserMapper;
import org.server.dao.UserDAO;
import org.server.sercice.IdGeneratorService;
import javax.annotation.Resource;
import org.junit.Test;

public class UserMapperTest extends BaseTest{


  @Resource
  private UserMapper userMapper;

  @Resource
  private IdGeneratorService idGeneratorService;


  @Test
  public void selectAllUsersTest(){
    List<UserDAO> users = userMapper.selectAllUsers();
    System.out.println(users);

  }


  @Test
  public void selectByIdTest(){

    UserDAO user = userMapper.selectById("2891681402151164817");
    System.out.println(user);
  }


  @Test
  public void selectAllTest(){

    UserDAO user = userMapper.selectAll();
    System.out.println(user);

  }


  @Test
  public void insertUserTest(){

    UserDAO build = UserDAO
        .builder()
        .id(idGeneratorService.getNextId())
        .username("sdfgfdgdf")
        .address("UDD")
        .build();

    int i = userMapper.insertUser(build);
    System.out.println(i);

  }

}
