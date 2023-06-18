package mapper;

import java.util.List;
import org.server.mapper.UserMapper;
import org.server.pojo.User;
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
    List<User> users = userMapper.selectAllUsers();
    System.out.println(users);

  }


  @Test
  public void selectByIdTest(){

    User user = userMapper.selectById("2891681402151164817");
    System.out.println(user);
  }


  @Test
  public void selectAllTest(){

    User user = userMapper.selectAll();
    System.out.println(user);

  }


  @Test
  public void insertUserTest(){

    User build = User
        .builder()
        .id(idGeneratorService.getNextId())
        .username("sdfgfdgdf")
        .address("UDD")
        .build();

    int i = userMapper.insertUser(build);
    System.out.println(i);

  }

}
