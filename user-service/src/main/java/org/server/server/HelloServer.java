package org.server.server;


import io.grpc.stub.StreamObserver;
import lombok.extern.log4j.Log4j2;
import org.server.rpc.generated.HelloRequest;
import org.server.rpc.generated.HelloResponse;
import org.server.rpc.generated.HelloServiceGrpc;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class HelloServer extends HelloServiceGrpc.HelloServiceImplBase {

  public void sayHello(HelloRequest request,
      StreamObserver<HelloResponse> responseObserver) {        // 根據請求對象建立響應對象，返回響應信息
    HelloResponse response = HelloResponse
        .newBuilder()
        .setMessage(String.format("Hello, %s. This message comes from gRPC.", request.getName()))
        .build();
    responseObserver.onNext(response);
    responseObserver.onCompleted();
    log.info("Client Message Received：[{}]", request.getName());
  }


}
