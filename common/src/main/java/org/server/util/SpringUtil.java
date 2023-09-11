package org.server.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
//為了在應用程序的其他部分中方便地獲取 Spring 容器中的 Bean 實例和環境屬性值
//可以不使用 @Autowired  @Resource 

@Component
public class SpringUtil implements ApplicationContextAware, EnvironmentAware {
  private static ApplicationContext applicationContext;
  private static Environment environment;

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    SpringUtil.applicationContext = applicationContext;
  }

  @Override
  public void setEnvironment(Environment environment) {
    SpringUtil.environment = environment;
  }

  public static <T> T getBean(Class<T> clz) {
    return applicationContext.getBean(clz);
  }

  public static String getProperty(String key) {
    return environment.getProperty(key);
  }
}
