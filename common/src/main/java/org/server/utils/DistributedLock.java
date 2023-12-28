package org.server.utils;

import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 分布式鎖(防止重複提交)<br>
 * 
 * 預設鎖30秒
 *
 */
@Slf4j
@Component
public class DistributedLock {

//	@Value("${redis.distributed.lock.seconds.timeout:10}")
	private Integer timeout = 30;

	@Resource
	private RedisTemplate<String, String> redisTemplate;

	/**
	 * 上鎖
	 * 
	 * @param key 上鎖key
	 * @return
	 */
	public boolean lockNotify(String key) {
		return lock(key, timeout, TimeUnit.SECONDS);
	}

	/**
	 * 上鎖(多個key會用.拼接)<br>
	 * 例 : redisKey=key1.key2.key3
	 * 
	 * @param lockSecondsTime 上锁时间(单位:秒)
	 * @param keys
	 * @return
	 */
	public boolean lock(Integer lockSecondsTime, String... keys) {
		return lock(String.join(".", keys), lockSecondsTime, TimeUnit.SECONDS);
	}
	
	/**
	 * 上鎖(多個key會用.拼接)<br>
	 * 例 : redisKey=key1.key2.key3
	 * 
	 * @param keys
	 * @return
	 */
	public boolean lock(String... keys) {
		return lock(String.join(".", keys), timeout, TimeUnit.SECONDS);
	}

	public boolean lock(String key, long expire, TimeUnit timeUnit) {
		Boolean result = redisTemplate.opsForValue().setIfAbsent(key, key, expire, timeUnit);
		log.info("redis lock key:[{}], result:[{}]", key, result);
		return result == null ? false : result;
	}

	/**
	 * 是否上鎖
	 * 
	 * @param key 上鎖key
	 * @return
	 */
	public boolean isLock(String key) {
		Boolean result = redisTemplate.hasKey(key);
		return result == null ? false : result;
	}
	
	/**
	 * 是否上鎖(多個key會用.拼接)<br>
	 * 
	 * @param key 上鎖key
	 * @return
	 */
	public boolean isLock(String... keys) {
		Boolean result = redisTemplate.hasKey(String.join(".", keys));
		return result == null ? false : result;
	}

	/**
	 * 解鎖
	 * 
	 * @param key 上鎖key
	 * @return
	 */
	public void unlock(String key) {
		redisTemplate.delete(key);
	}
	
	/**
	 * 解鎖(多個key會用.拼接)<br>
	 * 
	 * @param keys 上鎖key
	 * @return
	 */
	public void unlock(String... keys) {
		redisTemplate.delete(String.join(".", keys));
	}
}
