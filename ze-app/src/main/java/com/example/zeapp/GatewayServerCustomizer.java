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
        ssl.setKeyStore(createKeyStore());
        ssl.setKeyStorePassword(keyStorePassword);
        ssl.setKeyPassword(keyPassword);

        factory.setSsl(ssl);
        factory.setPort(port);
    }
    public String createKeyStore() throws KeyStoreException, CertificateException, IOException, NoSuchAlgorithmException, InvalidKeySpecException {

        String fullChainPath = "/keys/live/zubrilka-english.com/fullchain.pem";
        String privateKeyPath = "/keys/live/zubrilka-english.com/privkey.pem";
        String password = "password";

        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        keyStore.load(null, null);

        // Чтение файлов fullchain.pem и privkey.pem
        String fullChain = new String(Files.readAllBytes(Paths.get(fullChainPath)), StandardCharsets.UTF_8);
        String privateKey = new String(Files.readAllBytes(Paths.get(privateKeyPath)), StandardCharsets.UTF_8);

        // Извлечение закрытого ключа из privkey.pem
        String privateKeyPEM = privateKey.replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replace("\n", "");
        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyPEM);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKeyObject = keyFactory.generatePrivate(keySpec);

        // Извлечение сертификата из fullchain.pem
        String certificatePEM = fullChain.replace("-----BEGIN CERTIFICATE-----", "")
                .replace("-----END CERTIFICATE-----", "")
                .replace("\n", "");
        byte[] certificateBytes = Base64.getDecoder().decode(certificatePEM);
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        InputStream certificateInputStream = new ByteArrayInputStream(certificateBytes);
        X509Certificate certificate = (X509Certificate) certificateFactory.generateCertificate(certificateInputStream);

        // Добавление закрытого ключа и сертификата в KeyStore
        keyStore.setKeyEntry("alias", privateKeyObject, password.toCharArray(), new X509Certificate[]{certificate});

        // Преобразование KeyStore в строку Base64
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        keyStore.store(bos, password.toCharArray());
        return Base64.getEncoder().encodeToString(bos.toByteArray());
    }
}
