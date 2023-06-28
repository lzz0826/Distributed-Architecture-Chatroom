package org.server.service;

import java.util.Date;
import javax.annotation.Resource;
import org.server.dao.ChatRecordDAO;
import org.server.mapper.ChatRecordMapper;
import org.server.sercice.IdGeneratorService;
import org.server.websocket.enums.EWsMsgType;
import org.springframework.stereotype.Service;

@Service
public class ChatRecordService {


  @Resource
  private ChatRecordMapper chatRecordMapper;

  @Resource
  private IdGeneratorService idGeneratorService;


  public void addChatRecord(String senderUserId, String receiverUserId, String chatroomId,
      String content ,EWsMsgType eWsMsgType) {

    ChatRecordDAO dao = ChatRecordDAO
        .builder()
        .id(idGeneratorService.getNextId())
        .userId(senderUserId)
        .receiverUserId(receiverUserId)
        .chatroomId(chatroomId)
        .msgType(eWsMsgType.code)
        .content(content)
        .status(true)
        .updateTime(new Date())
        .createTime(new Date())
        .build();

    chatRecordMapper.insertChatRecord(dao);

  }


}
