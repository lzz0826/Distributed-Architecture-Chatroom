package org.server.websocket.mpa;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class WsChatRoom {
  private final static ConcurrentHashMap<String, List<String>> map = new ConcurrentHashMap<>();

  /**
   * key:       value:
   * chatroomId, userIds
   */

  public static void addUserToChatRoom(String chatroomId, String userId) {
    List<String> list = map.get(chatroomId);
    if (list == null) {
      list = new ArrayList<>();
      map.put(chatroomId, list);
    }
    list.add(userId);
    map.put(chatroomId,list);
  }

  public static void removeUserFromChatRoom(String chatroomId, String userId) {
    List<String> list = map.get(chatroomId);
    if (list != null) {
      list.remove(userId);
      map.put(chatroomId,list);
      if (list.isEmpty()) {
        map.remove(chatroomId);
      }
    }
  }

  public static void put(String chatroomId, List<String> userIds) {
    map.put(chatroomId, userIds);
  }

  public static List<String> get(String chatroomId) {
    return map.get(chatroomId);
  }

  public static void del(String chatroomId) {
    map.remove(chatroomId);
  }
}
