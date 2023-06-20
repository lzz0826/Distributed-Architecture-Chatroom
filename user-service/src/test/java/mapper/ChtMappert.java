package mapper;

import javax.annotation.Resource;
import org.junit.Test;
import org.server.dao.ChatroomDAO;
import org.server.mapper.ChatroomMapper;

public class ChtMappert extends BaseTest{

  @Resource
  private ChatroomMapper chatroomMapper;

  @Test
  public void chatroomMapperTest(){

    ChatroomDAO build = ChatroomDAO
        .builder()
        .id("123444")
        .name("ttt")
        .adminUserId("efefef")
        .status(false)
        .build();

    System.out.println(build);

    int i = chatroomMapper.insertChatroom(build);
    System.out.println(i);
  }


}
