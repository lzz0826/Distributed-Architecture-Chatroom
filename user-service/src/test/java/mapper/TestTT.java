package mapper;

import java.util.Set;
import org.junit.Test;
import org.server.websocket.cache.UsersCacheRoomCache;

public class TestTT extends BaseTest{


  @Test
  public void add(){

    UsersCacheRoomCache.setChatroomIds("00999","000");

  }

  @Test
  public void get(){
    Set<String> chatroomIds = UsersCacheRoomCache.getChatroomIds("00999");
    System.out.println(chatroomIds);
  }



}
