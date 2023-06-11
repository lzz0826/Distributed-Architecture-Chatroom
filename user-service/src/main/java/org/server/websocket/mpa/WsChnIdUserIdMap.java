package org.server.websocket.mpa;

import io.netty.channel.ChannelId;
import java.util.concurrent.ConcurrentHashMap;

public class WsChnIdUserIdMap {



  private final static ConcurrentHashMap<ChannelId,String> map = new ConcurrentHashMap();
  /**
   * key:       value:
   * channelId, userId
   */

  public static void put(ChannelId channelId, String userId){
    map.put(channelId,userId);
  }


  public static String get(ChannelId channelId){
    if(!map.containsKey(channelId)){
      return null;
    }
    return map.get(channelId);
  }

  public static void del(ChannelId channelId){
    map.remove(channelId);

  }










}
