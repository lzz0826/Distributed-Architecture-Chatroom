package org.server.websocket.cache;
import java.util.HashSet;
import java.util.Set;
import org.server.service.RedisService;
import org.server.util.SpringUtil;


public class UsersCacheRoomCache {
  private static RedisService redisService;

  //使用懶加載(處理static第一次初始會是空) + ApplicationContext.getBean(獲取spring boot管理的Bean)
  private static RedisService getRedisService(){
    if(redisService == null){
      redisService =  SpringUtil.getBean(RedisService.class);
    }
    return redisService;
  }


  public static String CACHE_ROOM_USERS_CACHE = "UsersCacheRoomCache-";




  /**
   * key:       value:
   * userId, chatroomId
   */

  public static Set<String> getChatroomIds(String userId){

    return (Set<String>) getRedisService().get(CACHE_ROOM_USERS_CACHE + userId);
  }

  public static void setChatroomIds(String userId, String chatroomId) {
    Set<String> chatroomIds = (Set<String>) getRedisService().get(CACHE_ROOM_USERS_CACHE + userId);

    if (chatroomIds == null) {
      chatroomIds = new HashSet<>();
    }

    chatroomIds.add(chatroomId);

    getRedisService().set(CACHE_ROOM_USERS_CACHE + userId, chatroomIds);
  }

}
