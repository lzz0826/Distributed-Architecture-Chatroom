package org.server.controller;

import javax.annotation.Resource;
import lombok.extern.log4j.Log4j2;
import org.server.config.GrpcClientConfig;
import org.server.rpc.generated.HelloRequest;
import org.server.rpc.generated.HelloResponse;
import org.server.rpc.generated.HelloServiceGrpc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
@Log4j2
public class HelloController {

  @Resource
  private GrpcClientConfig grpcClientConfig;

  @GetMapping("/hello")
  public String hello(
      @RequestParam(name = "name", defaultValue = "xxx", required = false) String name) {
    // 构建一个请求
    HelloRequest request = HelloRequest.newBuilder().setName(name).build();
    // 使用 stub 发送请求至服务端
    HelloServiceGrpc.HelloServiceBlockingStub stub = grpcClientConfig.helloServiceBlockingStub();
    HelloResponse response = stub.sayHello(request);
    log.info("Server response received: [{}]", response.getMessage());
    return response.getMessage();
  }
}
