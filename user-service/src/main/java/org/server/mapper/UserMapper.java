package org.server.mapper;

import java.util.List;
import org.server.dao.UserDAO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {

  UserDAO selectAll();

  int insertUser(UserDAO user);


  UserDAO selectById(@Param("id") String id);


  UserDAO selectByUsername(@Param("username") String username);

  List<UserDAO> selectAllUsers();









}
