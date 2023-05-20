package org.server.service;


import javax.annotation.Resource;
import org.server.mapper.UserMapper;
import org.server.pojo.User;
import org.springframework.stereotype.Service;

@Service
public class BasicService {

  @Resource
  private UserMapper userMapper;

  @Resource
  protected JwtService jwtService;


  protected User getUserByUsername(String username){
    return userMapper.selectByUsername(username);
  }


  protected User gitUserById(String id){
    return userMapper.selectById(id);
  }


}
