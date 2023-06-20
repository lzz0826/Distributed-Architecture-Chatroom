package org.server.service;


import java.util.Date;
import javax.annotation.Resource;
import org.server.dao.ChatroomDAO;
import org.server.exception.AddErrorException;
import org.server.mapper.ChatroomMapper;
import org.server.sercice.IdGeneratorService;
import org.server.vo.ChatroomVO;
import org.springframework.stereotype.Service;

@Service
public class ChatroomService {


  @Resource
  private ChatroomMapper chatroomMapper;

  @Resource
  private IdGeneratorService idGeneratorService;


  public ChatroomVO getChatroomById(String id){
    ChatroomDAO dao = chatroomMapper.selectById(id);
    return convertDAOToVO(dao);
  }

  public ChatroomVO convertDAOToVO(ChatroomDAO dao){
    if(dao == null){
      return null;
    }
    ChatroomVO vo = ChatroomVO
        .builder()
        .id(dao.getId())
        .name(dao.getName())
        .adminUserId(dao.getAdminUserId())
        .status(dao.getStatus())
        .updateTime(dao.getUpdateTime())
        .createTime(dao.getCreateTime())
        .build();
    return vo;
  }






  public void addChatroom(String name) throws AddErrorException {


    ChatroomDAO dao = ChatroomDAO
        .builder()
        .id(idGeneratorService.getNextId())
        .name(name)
//        TODO
        .adminUserId("e44354")
        .status(false)
        .updateTime(new Date())
        .createTime(new Date())
        .build();

    int i = chatroomMapper.insertChatroom(dao);

    if(i == 0){
      throw new AddErrorException();
    }

  }



}
