package org.server.rpc.client;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.AbstractStub;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

@Service
public abstract class BaseRpcClient<T, S, U extends AbstractStub<U>> {



    public S callGrpcService(String targetServiceName, String targetGroupName,
        Function<ManagedChannel, U> stubCreator,
        Function<U, T> requestCreator,
        BiFunction<U, T, S> responseHandler) throws NacosException {
        if (StringUtils.isBlank(targetServiceName)) {
            throw new IllegalArgumentException("请填写targetServiceName");
        }

        if (StringUtils.isBlank(targetGroupName)) {
            throw new IllegalArgumentException("请填写targetGroupName");
        }

        String nacosServerAddress = "127.0.0.1:8848"; // Nacos 服务器地址，请根据实际情况修改

        // 创建 NamingService 实例
        NamingService namingService;
        try {
            namingService = NacosFactory.createNamingService(nacosServerAddress);
        } catch (NacosException e) {
            throw new RuntimeException("创建 NamingService 实例失败", e);
        }

        // 使用 namingService 进行服务调用
        List<Instance> instances;
        try {
            instances = namingService.getAllInstances(targetServiceName, targetGroupName);
        } catch (NacosException e) {
            throw new RuntimeException("获取服务实例失败", e);
        }

        if (instances.isEmpty()) {
            throw new RuntimeException("找不到实例");
        }

        for (Instance instance : instances) {
            String ip = instance.getIp();
            int port = instance.getPort();

            ManagedChannel channel = ManagedChannelBuilder.forAddress(ip, port).usePlaintext().build();
            U stub = stubCreator.apply(channel);

            T request = requestCreator.apply(stub);

            S response = responseHandler.apply(stub, request);

            channel.shutdown();

            return response;
        }

        return null;
    }
}

