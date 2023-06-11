package org.server.websocket.mpa;

import io.netty.channel.ChannelId;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 存儲用戶與頻道之間的關係
 */

public class WsUserIdChnIdMap {

  /**
   * key:       value:
   * userId, channelId
   */


  private final static ConcurrentHashMap<String, ChannelId> map = new ConcurrentHashMap();

  public static void put(String userId,ChannelId channelId){
    map.put(userId,channelId);
  }

  public static ChannelId get(String userId){
    if(!map.containsKey(userId)){
      return null;
    }
    return map.get(userId);
  }


  public static void del(String userId){
    map.remove(userId);
  }


}
