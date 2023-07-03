package org.server.cache;

import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
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
   * 暫時使用 key: userId, val: chatroomId
   */
  public void putByUserId(String userId ,String chatroomId , Integer timeout){
    String key = CHATSILENCECACHE_PREFIX+userId;
    String value = CHATSILENCECACHE_PREFIX+chatroomId;
    redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
  }

  public String getByUserId(String userId){
    String key = CHATSILENCECACHE_PREFIX+userId;
    String chatroomId = redisTemplate.opsForValue().get(key);
    return chatroomId;

  }

  public void delByUserId(String userId) {
    String key = CHATSILENCECACHE_PREFIX+userId;
    redisTemplate.delete(key);
  }

}
