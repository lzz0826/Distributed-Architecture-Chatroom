package org.server.config;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import javax.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.server.utils.IpUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Log4j2
@Component
public class OrderNacosConfig {

  @Value("${spring.cloud.nacos.discovery.server-addr}")


  private String serverAddress;

  /**
   * 向Nacos註冊GRPC
   * @throws NacosException
   */
  @PostConstruct
  public void registerGrpcService() throws NacosException {
    NamingService namingService = NamingFactory.createNamingService(serverAddress);

    String serviceName = "order-grpc";
    String groupName = "grpc";
    String ip = IpUtil.getLocalIpAddress();
    int port = 50071;

    log.info("以 serviceName:{}, groupName:{}, ip: {}, port: {} 註冊Nacos服務", serviceName, groupName, ip, port);

    namingService.registerInstance(serviceName, groupName, ip, port);
  }





}
