package org.server.config;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import lombok.extern.log4j.Log4j2;
import org.server.server.HelloServer;

import org.springframework.stereotype.Component;

@Log4j2
@Component
public class GrpcServerConfig {

    private Server server;





    @PostConstruct
    private void start() throws IOException {
        server = ServerBuilder.forPort(50051)
                .addService(new HelloServer())
                .build()
            .start();
        log.info("gRPC server started, listening on {}", 50051);
    }

    @PreDestroy
    private void stop() {
        if (server != null) {
            server.shutdown();
        }
    }
}
