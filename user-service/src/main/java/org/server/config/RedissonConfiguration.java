package org.server.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfiguration {

  @Value("${spring.redis.host}")
  private String redisHost;


  @Value("${spring.redis.password}")
  private String redisPassword;


  @Value("${spring.redis.port}")
  private int redisPort;

  @Value("${spring.redis.database}")
  private int database;


  @Bean(destroyMethod = "shutdown")
  public RedissonClient redisson() {
    Config config = new Config();
    String address = "redis://" + redisHost + ":" + redisPort;
    config.useSingleServer()
        .setAddress(address)
        .setPassword(redisPassword)
        .setDatabase(database);

    return Redisson.create(config);
  }




}
