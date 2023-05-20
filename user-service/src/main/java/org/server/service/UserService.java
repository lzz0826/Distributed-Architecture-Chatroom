package org.server.service;


import javax.annotation.Resource;
import org.server.mapper.UserMapper;
import org.server.pojo.User;
import org.server.vo.UserVO;
import org.springframework.stereotype.Service;

@Service
public class UserService {


  @Resource
  private UserMapper userMapper;

  public UserVO gitUserById(String id){

    User user = userMapper.selectById(id);
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



}
