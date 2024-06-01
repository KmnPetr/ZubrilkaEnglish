package com.example.zeapp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.boot.web.reactive.server.ReactiveWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
//public class NettyConfig {
//    @Value("${server.ssl.key-store}")
//    private String keyStore;
//
//    @Value("${server.ssl.key-store-password}")
//    private String keyStorePassword;
//
//    @Value("${server.ssl.key-password}")
//    private String keyPassword;
//
//    @Value("${server.port}")
//    private int port;
//
//    @Bean
//    public ReactiveWebServerFactory reactiveWebServerFactory() {
//        NettyReactiveWebServerFactory factory = new NettyReactiveWebServerFactory() {
//
//            @Override
//            protected void configureHttpServer(HttpServer httpServer) {
//                httpServer.secure(sslContextSpec -> sslContextSpec.sslContext(createSslContext()));
//                super.configureHttpServer(httpServer);
//            }
//        };
//        factory.setPort(port);
//        return factory;
//    }
//
//}
