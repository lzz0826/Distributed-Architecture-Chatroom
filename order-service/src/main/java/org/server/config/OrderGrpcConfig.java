package org.server.config;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import lombok.extern.log4j.Log4j2;

import org.springframework.stereotype.Component;

import java.io.IOException;

@Log4j2
@Component
public class OrderGrpcConfig {

  private Server server;




//目前無
  @PostConstruct
  private void start() throws IOException {
    int orderGrpcPort = 50051;
    server = ServerBuilder.forPort(orderGrpcPort)
//        .addService(new RpcVersiondetailService(versiondetailService))
        .build()
        .start();
    log.info("gRPC server started, listening on {}", orderGrpcPort);
  }

  @PreDestroy
  private void stop() {
    if (server != null) {
      server.shutdown();
    }
  }


}
