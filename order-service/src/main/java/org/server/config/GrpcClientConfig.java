package org.server.config;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.server.rpc.generated.HelloServiceGrpc;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcClientConfig {

//    @Value("${spring.cloud.nacos.discovery.server-addr}")
//    private String serverAddress;

    @Bean
    public HelloServiceGrpc.HelloServiceBlockingStub helloServiceBlockingStub() {
        ManagedChannel channel = ManagedChannelBuilder.forTarget("127.0.0.1:50051")
                .usePlaintext()
            .build();
        return HelloServiceGrpc.newBlockingStub(channel);
    }
}
