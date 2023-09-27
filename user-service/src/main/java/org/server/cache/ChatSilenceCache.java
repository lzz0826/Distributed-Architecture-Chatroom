package org.server.cache;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import org.server.dao.ChatSilenceCacheDAO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;


@Component
public class ChatSilenceCache {

  @Resource
  private RedisTemplate<String, String> redisTemplate;


  @Value("${chatroom.silenceCache_prefix}")
  public String CHATSILENCECACHE_PREFIX;


  /**
   * 暫時使用 key: userId, val: ChatSilenceCacheDAO
   *
   * ChatSilenceCacheDAO
   * userId
   * chatroomId
   * timeout
   *
   */
  public void putByUserId(String userId, String chatroomId, Integer timeout) {
    String key = CHATSILENCECACHE_PREFIX + userId;

    String mapStr = redisTemplate.opsForValue().get(key);

    Map<String, ChatSilenceCacheDAO> chatSilenceCacheDAOMap = new HashMap<>();

    if (mapStr != null) {
      chatSilenceCacheDAOMap = JSON.parseObject(mapStr, new TypeReference<Map<String, ChatSilenceCacheDAO>>() {});
    }

    long maturityTimout = System.currentTimeMillis() + (timeout * 1000);
    ChatSilenceCacheDAO dao = ChatSilenceCacheDAO.builder()
        .userId(userId)
        .chatroomId(chatroomId)
        .timeout(maturityTimout)
        .build();

    chatSilenceCacheDAOMap.put(chatroomId, dao);

    redisTemplate.opsForValue().set(key, JSON.toJSONString(chatSilenceCacheDAOMap));
  }


  public Map<String, ChatSilenceCacheDAO> getByUserId(String userId){


    String key = CHATSILENCECACHE_PREFIX + userId;

    String mapStr = redisTemplate.opsForValue().get(key);

    Map<String, ChatSilenceCacheDAO> chatSilenceCacheDAOMap = new HashMap<>();

    if (mapStr != null) {
      chatSilenceCacheDAOMap = JSON.parseObject(mapStr, new TypeReference<Map<String, ChatSilenceCacheDAO>>() {});
    }

    return chatSilenceCacheDAOMap;

  }

  public void delChatRoomByUserId(String userId , String chatroomId){

    String key = CHATSILENCECACHE_PREFIX + userId;

    String mapStr = redisTemplate.opsForValue().get(key);

    if (mapStr != null) {
      Map<String, ChatSilenceCacheDAO> chatSilenceCacheDAOMap =
          JSON.parseObject(mapStr, new TypeReference<Map<String, ChatSilenceCacheDAO>>() {});

      chatSilenceCacheDAOMap.remove(chatroomId);

      redisTemplate.opsForValue().set(key, JSON.toJSONString(chatSilenceCacheDAOMap));
    }
  }

  public void delByUserId(String userId) {


    String key = CHATSILENCECACHE_PREFIX+userId;
    redisTemplate.delete(key);
  }

}


