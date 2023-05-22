package org.server.cache;

import com.alibaba.fastjson.JSON;
import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;
import org.server.vo.UserVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenIdCache {


    @Value("${jwt.timeout}")
    public long timeout;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 暫時使用 key: jwtToken, val: userVO
     */
    public void putByJwtToken(String jwtToken, UserVO userVO) {

        String objStr = JSON.toJSONString(userVO);
        redisTemplate.opsForValue().set(getKey(jwtToken), objStr, timeout, TimeUnit.SECONDS);
    }

    public UserVO getByJwtToken(String jwtToken) {
        String objStr = redisTemplate.opsForValue().get(getKey(jwtToken));
        return JSON.parseObject(objStr, UserVO.class);
    }

    public void delByJwtToken(String jwtToken) {
        redisTemplate.delete(getKey(jwtToken));
    }

    private String getKey(String jwtToken) {
        return "JWT_CACHE::" + jwtToken;
    }
}
