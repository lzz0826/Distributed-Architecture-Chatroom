package org.server.websocket.mpa;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class WsChatRoom {

  //TODO 需要處理起多台一致
  private final static ConcurrentHashMap<String, Set<String>> map = new ConcurrentHashMap<>();

  /**
   * key:       value:
   * chatroomId, userIds
   */



  public static void addUserToChatRoom(String chatroomId, String userId) {
    Set<String> list = map.get(chatroomId);
    if (list == null) {
      list = new HashSet<>();
      map.put(chatroomId, list);
    }
    list.add(userId);
    map.put(chatroomId,list);
  }

  public static void removeUserFromChatRoom(String chatroomId, String userId) {
    Set<String> list = map.get(chatroomId);
    if (list != null) {
      list.remove(userId);
      map.put(chatroomId,list);
      if (list.isEmpty()) {
        map.remove(chatroomId);
      }
    }
  }

  public static void removeUserChatRoomAll(String userId) {
    for (Entry<String, Set<String>> entry : map.entrySet()) {
      Set<String> list = entry.getValue();
      Iterator<String> iterator = list.iterator();
      while (iterator.hasNext()) {
        String s = iterator.next();
        if (s.contains(userId)) {
          iterator.remove();
        }
      }
    }
  }

  public static void put(String chatroomId, Set<String> userIds) {
    map.put(chatroomId, userIds);
  }

  public static Set<String> get(String chatroomId) {
    return map.get(chatroomId);
  }

  public static void del(String chatroomId) {
    map.remove(chatroomId);
  }
}
