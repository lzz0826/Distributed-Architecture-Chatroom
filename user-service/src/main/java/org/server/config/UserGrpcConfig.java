package org.server.config;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import lombok.extern.log4j.Log4j2;

import rpc.server.HelloServer;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class UserGrpcConfig {

    private Server server;

    @PostConstruct
    private void start() throws IOException {
        int serverGrpcPort = 50052;
        server = ServerBuilder.forPort(serverGrpcPort)
//            .addService(new RpcDispublogService(dispublogService))
            .addService(new HelloServer())
            .build()
            .start();
        log.info("gRPC server started, listening on {}", serverGrpcPort);
    }

    @PreDestroy
    private void stop() {
        if (server != null) {
            server.shutdown();
        }
    }
}
