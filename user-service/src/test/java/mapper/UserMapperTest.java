package mapper;

import java.util.Date;
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
  public void updateUserTest(){

    UserDAO build = UserDAO
        .builder()
        .id("3099538860151615210")
        .avatarPth("333rr3r")
        .address("efef")
        .updateTime(new Date())
        .build();

    int i = userMapper.updateUser(build);
    System.out.println(i);
  }


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
        .username("sd333fdgdf")
        .password("efw33ef")
        .avatarPth("ef33ef")
        .address("UD33D")
        .role("ee33e")
        .createTime(new Date())
        .updateTime(new Date())
        .build();

    int i = userMapper.insertUser(build);
    System.out.println(i);

  }

}
