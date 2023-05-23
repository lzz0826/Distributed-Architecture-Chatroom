//package org.server.rpc.client;
//
//import io.grpc.ManagedChannel;
//import io.grpc.ManagedChannelBuilder;
//import java.util.concurrent.TimeUnit;
//import org.server.rpc.generated.HelloRequest;
//import org.server.rpc.generated.HelloResponse;
//import org.server.rpc.generated.HelloServiceGrpc;
//import org.springframework.stereotype.Service;
//
//@Service
//public class HelloClient {
//
// */
//
//  @RestController
//  @Slf4jpublic
//  class HelloController {
//
//    @Autowired
//    GrpcClientConfiguration configuration;
//
//    @GetMapping("/hello")
//    public String hello(
//        @RequestParam(name = "name", defaultValue = "JiangWen", required = false) String name) {        // 構建一個請求
//      HelloWorldService.HelloRequest request = HelloWorldService.HelloRequest
//          .newBuilder()
//          .setName(name)
//          .build();        // 使用stub發送請求至服務端
//      HelloWorldService.HelloResponse response = configuration.getStub().sayHello(request);
//      log.info("Server response received: [{}]", response.getMessage());
//      return response.getMessage();
//    }
//  }