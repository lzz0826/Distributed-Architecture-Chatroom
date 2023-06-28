package mapper;

import java.util.Date;
import javax.annotation.Resource;
import org.junit.Test;
import org.server.dao.ChatRecordDAO;
import org.server.dao.ChatroomDAO;
import org.server.mapper.ChatRecordMapper;
import org.server.websocket.enums.EWsMsgType;

public class ChatRecordMapperTest extends BaseTest {


  @Resource
  private ChatRecordMapper chatRecordMapper;

  @Test
  public void chatRecordMapperTest(){

    ChatRecordDAO build = ChatRecordDAO
        .builder()
        .id("22442")
        .userId("333")
        .receiverUserId("4444")
        .chatroomId("2222")
        .content("test9999")
        .msgType(EWsMsgType.PrivateChat.code)
        .status(true)
        .updateTime(new Date())
        .createTime(new Date())
        .build();

    int i = chatRecordMapper.insertChatRecord(build);

  }


}
