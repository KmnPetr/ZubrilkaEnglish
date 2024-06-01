package com.example.zeapp;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.boot.web.server.Ssl;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

import java.security.KeyStore;
import java.security.cert.X509Certificate;

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


    @SneakyThrows
    @Override
    public void customize(NettyReactiveWebServerFactory factory) {

        log.info("NettyReactiveWebServerFactory FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFfFFFFFFFFFFFFFFFFFFFFFFFFFFFF");

        Ssl ssl = new Ssl();


        ssl.setEnabled(true);
        ssl.setKeyStore(keyStore);
        ssl.setKeyStorePassword(keyStorePassword);
        ssl.setKeyPassword(keyPassword);

        factory.setSsl(ssl);
        factory.setPort(port);
    }
}
