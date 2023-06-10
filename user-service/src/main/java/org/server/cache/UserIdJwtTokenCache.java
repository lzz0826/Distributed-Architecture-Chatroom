package org.server.cache;


import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;


@Component
public class UserIdJwtTokenCache {

  @Value("${jwt.timeout}")
  public long timeout;
  @Resource
  private RedisTemplate<String, String> redisTemplate;

  /**
   * 暫時使用 key: userId, val: jwtToken
   */
  public void putByUserId(String userId, String jwtToken) {
    redisTemplate.opsForValue().set(getKey(userId), jwtToken, timeout, TimeUnit.SECONDS);
  }

  public String getByUserId(String userId) {
    return redisTemplate.opsForValue().get(getKey(userId));
  }

  public void delByUserId(String userId) {
    redisTemplate.delete(getKey(userId));
  }

  private String getKey(String adminId) {
    return "UserId_CACHE::" + adminId;
  }
}

