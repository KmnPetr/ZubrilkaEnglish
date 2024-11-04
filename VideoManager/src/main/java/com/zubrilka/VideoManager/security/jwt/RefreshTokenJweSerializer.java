package com.zubrilka.VideoManager.security.jwt;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import com.nimbusds.jwt.EncryptedJWT;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;
import java.util.function.Function;

/**
 * serializes Tocken to a string
 * refresh token protects payload with encryption like jwe
 */
@Slf4j
@Component
public class RefreshTokenJweSerializer implements Function<Token, String> {


    private final JWEEncrypter jweEncrypter;

    private JWEAlgorithm jweAlgorithm = JWEAlgorithm.DIR;

    private EncryptionMethod encryptionMethod = EncryptionMethod.A128GCM;

    public RefreshTokenJweSerializer(@Value("${refresh-token-key}") String refreshTokenKey) throws ParseException, KeyLengthException {
        this.jweEncrypter = new DirectEncrypter(OctetSequenceKey.parse(refreshTokenKey));
    }

    @Override
    public String apply(Token token) {
        var jwsHeader = new JWEHeader.Builder(this.jweAlgorithm, this.encryptionMethod)
                .keyID(token.uuid().toString())
                .build();
        var claimsSet = new JWTClaimsSet.Builder()
                .jwtID(token.uuid().toString())
                .subject(token.subject())
                .issueTime(Date.from(token.createdAt()))
                .expirationTime(Date.from(token.expiresAt()))
                .claim("authorities", token.authorities())
                .build();
        var encryptedJWT = new EncryptedJWT(jwsHeader, claimsSet);
        try {
            encryptedJWT.encrypt(this.jweEncrypter);

            return encryptedJWT.serialize();
        } catch (JOSEException exception) {
            log.error(exception.getMessage(), exception);
        }

        return null;
    }
}
