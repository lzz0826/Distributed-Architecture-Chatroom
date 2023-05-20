package org.server.service;


import java.nio.charset.StandardCharsets;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.server.exception.LoginErrorException;
import org.server.mapper.UserMapper;
import org.server.pojo.User;
import org.server.vo.LoginVO;
import org.server.vo.UserVO;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
public class UserService extends BasicService{



  public UserVO gitUserVO(String id){
    User user = gitUserById(id);
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
    User userDAO = getUserByUsername(username);
    if(userDAO == null){
      throw new LoginErrorException();
    }
    String md5token = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));

    if(StringUtils.isBlank(userDAO.getPassword())){
      throw new LoginErrorException();
    }
    if(!md5token.equals(userDAO.getPassword())){
      throw new LoginErrorException();
    }

    String jwtToken = jwtService.generateToken(userDAO);

    return LoginVO
        .builder()
        .jwtToken(jwtToken)
        .build();


  }



}
