package org.server.mapper;

import java.util.List;
import org.server.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {

  User selectAll();

  int insertUser(User user);


  User selectById(@Param("id") String id);


  User selectByUsername(@Param("username") String username);

  List<User> selectAllUsers();









}
