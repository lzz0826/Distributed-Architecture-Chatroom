package org.server.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.server.dao.ChatroomDAO;

@Mapper
public interface ChatroomMapper {

  int insertChatroom(ChatroomDAO dao);


}
