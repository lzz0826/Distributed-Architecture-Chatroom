package org.server.websocket;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class NettyInitListen implements CommandLineRunner {

    @Value("${netty.port}")
    private Integer nettyPort;

    @Override
    public void run (String ... args) {
        try {
            log.info("Netty 以 port: {} 啟動", nettyPort);
            new NettyServer(nettyPort).start();
        } catch (Exception e) {
            log.error("Netty 錯誤: {}", e.getMessage());
        }
    }

}
