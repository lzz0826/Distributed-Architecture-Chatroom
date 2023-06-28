package org.server.service;


import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.server.entity.CustomUserDetails;
import org.server.exception.AddErrorException;
import org.server.exception.LoginErrorException;
import org.server.mapper.UserMapper;
import org.server.dao.UserDAO;
import org.server.sercice.IdGeneratorService;
import org.server.vo.LoginVO;
import org.server.vo.UserVO;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
public class UserService{


  @Resource
  private JwtCacheService jwtCacheService;

  @Resource
  private IdGeneratorService idGeneratorService;


  @Resource
  private UserMapper userMapper;

  @Resource
  private JwtService jwtService;

  @Resource
  private CustomUserDetailsService customUserDetailsService;


  public void addUser(String username , String password ,String address)
      throws AddErrorException {

    String md5token = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));

    UserDAO user = UserDAO
        .builder()
        .id(idGeneratorService.getNextId())
        .username(username)
        .password(md5token)
        .address(address)
//        TODO 角色
        .role("ROLE_ADMIN")
        .updateTime(new Date())
        .createTime(new Date())
        .build();


    int i = userMapper.insertUser(user);

    if(i == 0){
      throw new AddErrorException();
    }

  }


  public UserDAO getUserByUsername(String username){
    return userMapper.selectByUsername(username);
  }


  public UserDAO getUserById(String id){
    return userMapper.selectById(id);
  }

  public List<UserDAO> getAllUsers(){

    return userMapper.selectAllUsers();
  }

  public List<UserVO> getAllUserVOs(){
    List<UserDAO> allUsers = getAllUsers();

    if (allUsers.size() == 0 || allUsers.isEmpty()) {
      return null;
    }
    List<UserVO> userVOs = new ArrayList<>();
    for (UserDAO allUser : allUsers) {
      UserVO userVO = UserVO.builder()
          .id(allUser.getId())
          .username(allUser.getUsername())
          .address(allUser.getAddress())
          .build();

      userVOs.add(userVO);
    }
    return userVOs;
  }

  public UserVO getUserVO(String id){
    UserDAO user = getUserById(id);
    if(user == null){
      return null;
    }
    return UserVO
        .builder()
        .id(user.getId())
        .username(user.getUsername())
        .address(user.getAddress())
        .build();

  }


  public LoginVO login(String username , String password) throws LoginErrorException {



    String md5token = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));

    CustomUserDetails customUserDetails = customUserDetailsService.loadUserByUsername(username);
    if(!md5token.equals(customUserDetails.getPassword())){
      throw new LoginErrorException();
    }


    String jwtToken = jwtService.generateToken(customUserDetails);

    jwtCacheService.newToken(jwtToken,customUserDetails);

    return LoginVO
        .builder()
        .jwtToken(jwtToken)
        .build();


  }









}
