package org.server.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.server.dao.ChatroomDAO;
import org.server.dao.UserDAO;

@Mapper
public interface ChatroomMapper {

  int insertChatroom(ChatroomDAO dao);

  ChatroomDAO selectById(@Param("id") String id);






}
