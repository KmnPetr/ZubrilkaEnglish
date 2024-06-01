package com.example.zeapp;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.boot.web.server.Ssl;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;


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

        Ssl ssl = new Ssl();
        ssl.setEnabled(true);
        ssl.setKeyStore(keyStore);
        ssl.setKeyStorePassword(keyStorePassword);
        ssl.setKeyPassword(keyPassword);

        factory.setSsl(ssl);
        factory.setPort(port);
    }
}
