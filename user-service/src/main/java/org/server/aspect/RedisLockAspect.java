package org.server.aspect;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.server.annotation.RedisLock;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Aspect
@Component
public class RedisLockAspect {

  @Resource
  private RedissonClient redissonClient;

  /**
   * 加鎖切面
   *
   */
  @Around(value = "@annotation(redisLock)")
  public Object lock(ProceedingJoinPoint joinPoint, RedisLock redisLock) throws Throwable {
    String key = getRedisKey(joinPoint, redisLock);
    RLock lock = redissonClient.getLock(key);
    try {
      if (tryLock(lock, redisLock.waitTime(), redisLock.leaseTime(), redisLock.timeUnit())) {
        return joinPoint.proceed();
      }
      throw new RuntimeException("獲取鎖失敗");
    } finally {
      unlock(lock);
    }
  }

  /**
   * 嘗試獲取分佈式鎖
   * waitTime - 嘗試獲取鎖的最大等待時間
   * leaseTime - 獲取到鎖后持有鎖的時間。
   * timeUnit - 時間單位
   */
  private boolean tryLock(RLock lock, long waitTime, long leaseTime, TimeUnit timeUnit) throws InterruptedException {
    return lock.tryLock(waitTime, leaseTime, timeUnit);
  }

  /**
   * 釋放分佈式鎖
   */
  private void unlock(RLock lock) {
    if (lock.isHeldByCurrentThread()) {
      lock.unlock();
    }
  }


  /**
   * 獲取 Redis key，使用 SpEL 表達式解析註解中的 key 值
   */
  private String getRedisKey(ProceedingJoinPoint joinPoint, RedisLock redisLock) {
    Object[] args = joinPoint.getArgs();
    String[] keys = redisLock.keys();
    List<String> values = new ArrayList<>(keys.length);
    for (String key : keys) {
      values.add(parseKey(key, args));
    }
    return redisLock.name() + String.join("", values);
  }


  // 解析 SpEL 表達式
  private String parseKey(String keyExpression, Object[] args) {
    if (!StringUtils.hasText(keyExpression)) {
      return "";
    }
    if (keyExpression.startsWith("'") && keyExpression.endsWith("'")) {
      return keyExpression.substring(1, keyExpression.length() - 1);
    }
    ExpressionParser parser = new SpelExpressionParser();
    EvaluationContext context = new StandardEvaluationContext();
    for (int i = 0; i < args.length; i++) {
      context.setVariable("p" + i, args[i]);
    }
    Object value = parser.parseExpression(keyExpression).getValue(context);
    if (value == null) {
      return "";
    }
    return value.toString();
  }

}
