package org.server.rpc.base;


import org.server.rpc.client.BaseRpcClient;
import org.server.rpc.generated.HelloRequest;
import org.server.rpc.generated.HelloResponse;
import org.server.rpc.generated.HelloServiceGrpc.HelloServiceBlockingStub;
import org.springframework.stereotype.Service;

@Service
public class HelloClientBase extends
    BaseRpcClient<HelloRequest, HelloResponse, HelloServiceBlockingStub> {


  }