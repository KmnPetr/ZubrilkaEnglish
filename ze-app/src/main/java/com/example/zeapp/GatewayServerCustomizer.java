package com.example.zeapp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.boot.web.server.Ssl;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;



@Component
@Slf4j
public class GatewayServerCustomizer implements
        WebServerFactoryCustomizer<NettyReactiveWebServerFactory> {
    @Value("${server.ssl.key-store}")
    private String keyStore;

    @Value("${server.ssl.key-store-password}")
    private String keyStorePassword;

    @Value("${server.ssl.key-password}")
    private String keyPassword;

    @Value("${server.port}")
    private int port;

    private Ssl ssl;



//    @SneakyThrows
    @Override
    public void customize(NettyReactiveWebServerFactory factory) {

        ssl = new Ssl();
        ssl.setEnabled(true);
        ssl.setKeyStore(keyStore);
        ssl.setKeyStorePassword(keyStorePassword);
        ssl.setKeyPassword(keyPassword);

        factory.setSsl(ssl);
        factory.setPort(port);
    }


    private Integer i = 0;
    @Scheduled(fixedDelay = 2000)
    private void upgradeCache(){
        log.info("Работает ssl шедулер: i = "+i);
        if (i == 50) ssl = null;
        i++;
    }
}
