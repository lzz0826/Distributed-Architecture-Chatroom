package org.server.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import java.util.Date;
import javax.annotation.Resource;
import org.server.controller.req.chatroomRecord.ListReq;
import org.server.dao.ChatRecordDAO;
import org.server.mapper.ChatRecordMapper;
import org.server.sercice.IdGeneratorService;
import org.server.vo.ChatroomRecordVO;
import org.server.websocket.enums.EMsgType;
import org.server.websocket.enums.EWsMsgType;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class ChatRecordService {


  @Resource
  private ChatRecordMapper chatRecordMapper;

  @Resource
  private IdGeneratorService idGeneratorService;


  public void addChatRecord(String senderUserId, String receiverUserId, String chatroomId,
      String content ,EWsMsgType eWsMsgType, EMsgType eMsgType) {

    ChatRecordDAO dao = ChatRecordDAO
        .builder()
        .id(idGeneratorService.getNextId())
        .senderUserId(senderUserId)
        .receiverUserId(receiverUserId)
        .chatroomId(chatroomId)
        .msgType(eWsMsgType.code)
        .systemMsgType(eMsgType.code)
        .content(content)
        .status(true)
        .updateTime(new Date())
        .createTime(new Date())
        .build();

    chatRecordMapper.insertChatRecord(dao);

  }


  public Page<ChatroomRecordVO> selectChatRecords(ListReq req){

    Page<ChatroomRecordVO> vos = new Page();
    Integer page = req.getPage();
    Integer pageSize = req.getPageSize();
    Page<ChatRecordDAO> daos = PageHelper.startPage(page, pageSize)
        .doSelectPage(() -> chatRecordMapper.selectChatRecords(req));
    if(daos.isEmpty()){
      return vos;
    }
    BeanUtils.copyProperties(daos,vos);

    for (ChatRecordDAO chatRecordDAO : daos) {
      vos.add(convertDAOToVO(chatRecordDAO));

    }
    return vos;
  }

  public ChatroomRecordVO convertDAOToVO(ChatRecordDAO dao){
    if(dao == null){
      return null;
    }
    ChatroomRecordVO vo = ChatroomRecordVO
        .builder()
        .id(dao.getId())
        .senderUserId(dao.getSenderUserId())
        .receiverUserId(dao.getReceiverUserId())
        .chatroomId(dao.getChatroomId())
        .content(dao.getContent())
        .msgType(dao.getMsgType())
        .systemMsgType(dao.getSystemMsgType())
        .status(dao.getStatus())
        .updateTime(dao.getUpdateTime())
        .createTime(dao.getCreateTime())
        .build();
    return vo;
  }


}
