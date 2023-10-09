package mapper;

import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.junit.Test;
import org.server.controller.req.chatroomRecord.ListReq;
import org.server.dao.ChatRecordDAO;
import org.server.mapper.ChatRecordMapper;
import org.server.websocket.enums.EMsgType;
import org.server.websocket.enums.EWsMsgType;

public class ChatRecordMapperTest extends BaseTest {


  @Resource
  private ChatRecordMapper chatRecordMapper;



  @Test
  public void chatRecordDAOSTest(){

    ChatRecordDAO build = ChatRecordDAO.builder()
        .chatroomId("3105585082645819114")
        .build();

//    List<ChatRecordDAO> chatRecordDAOS = chatRecordMapper.selectChatRecords(build);
//
//    System.out.println(chatRecordDAOS);


  }

  @Test
  public void chatRecordMapperTest(){

    ChatRecordDAO build = ChatRecordDAO
        .builder()
        .id("TEED")
        .senderUserId("aaaa")
        .receiverUserId("bbbb")
        .chatroomId("vvvv")
        .content("ddddd")
        .status(true)
        .updateTime(new Date())
        .createTime(new Date())
        .build();

    int i = chatRecordMapper.insertChatRecord(build);

  }


  @Test
  public void selectChatRecordsTest(){

    ListReq build = ListReq.builder()
        .id("3587852724376419744")
        .build();

    List<ChatRecordDAO> daos = chatRecordMapper.selectChatRecords(build);

    for (ChatRecordDAO dao : daos) {
      System.out.println(dao);

    }


  }


}
