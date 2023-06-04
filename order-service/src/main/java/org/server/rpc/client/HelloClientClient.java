package org.server.rpc.client;


import com.alibaba.nacos.api.exception.NacosException;
import javax.annotation.Resource;
import lombok.extern.log4j.Log4j2;
import org.server.rpc.base.HelloClientBase;
import org.server.rpc.generated.HelloRequest;
import org.server.rpc.generated.HelloResponse;
import org.server.rpc.generated.HelloServiceGrpc;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class HelloClientClient {

  @Resource
  private HelloClientBase helloClientBase;

  public String sayHello(String name) throws NacosException {

    String targetServiceName = "user-grpc";
    String targetGroupName = "grpc";
    HelloResponse res = helloClientBase.callGrpcService(
        targetServiceName,
        targetGroupName,
        HelloServiceGrpc::newBlockingStub,
        unused -> HelloRequest.newBuilder().setName(name).build(),
        HelloServiceGrpc.HelloServiceBlockingStub::sayHello
    );

    String message = res.getMessage();
    return message ;
  }


}
