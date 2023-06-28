package org.server.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.server.dao.ChatRecordDAO;

@Mapper
public interface ChatRecordMapper {


  int insertChatRecord(ChatRecordDAO dao);






}
