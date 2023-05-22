package org.server.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RedisLock {

  /**
   * key的名稱前綴，與keys字段共同拼接出一個redis key
   * 如 name = user keys = 123（userId）
   * 最終使用的key為 user123
   */
  String name();


  /**
   * 能夠確定出系統中唯一性資源的key
   * 如 用戶， 使用用戶id為key
   * 或者使用 用戶名+手機號
   * 必須為spel表達式 如 #id #user.id #user.name
   * keys = {"#userId", "#userName"})
   */
  String[] keys();


  /**
   * 嘗試獲取鎖的最大等待時間(排隊時間)
   */
  int waitTime();

  /**
   * 獲取到鎖后持有鎖的時間(失效時間)
   * 確保不會造成死鎖需要考慮實踐的可能運行時間避免正常執行中失去鎖的功能
   */
  int leaseTime();

  /**
   * 時間單位
   */
  TimeUnit timeUnit();



}
