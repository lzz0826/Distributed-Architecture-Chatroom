package org.server.config;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
@Log4j2
public class ExecutorConfig {

  @Value("${async.executor.thread.core_pool_size}")
  private int corePoolSize;
  @Value("${async.executor.thread.max_pool_size}")
  private int maxPoolSize;
  @Value("${async.executor.thread.queue_capacity}")
  private int queueCapacity;
  @Value("${async.executor.thread.name.prefix}")
  private String namePrefix;

  @Bean(name = "orderServiceExecutor")
  public Executor asyncServiceExecutor() {
    log.info("start asyncServiceExecutor");
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    //配置核心線程數
    executor.setCorePoolSize(corePoolSize);
    //配置最大線程數
    executor.setMaxPoolSize(maxPoolSize);
    //配置隊列大小
    executor.setQueueCapacity(queueCapacity);
    //配置線程池中的線程的名稱前綴
    executor.setThreadNamePrefix(namePrefix);

    // rejection-policy：當pool已經達到max size的時候，如何處理新任務
    // CALLER_RUNS：不在新線程中執行任務，而是有調用者所在的線程來執行
    executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
    //執行初始化
    executor.initialize();
    return executor;
  }
}
