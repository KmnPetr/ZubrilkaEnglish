package com.zubrilka.VideoManager.security.jwt;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;
import java.util.function.Function;

/**
 * serializes the Token object to the token's jwt string
 * applies only to access tokens
 */
@Slf4j
@Component
public class AccessTokenJwsSerializer implements Function<Token, String> {

    private final JWSSigner jwsSigner;

    private JWSAlgorithm jwsAlgorithm = JWSAlgorithm.HS256;
    @Autowired
    public AccessTokenJwsSerializer(
            @Value("${access-token-key}") String accessTokenKey
    ) throws ParseException, KeyLengthException {
        this.jwsSigner = new MACSigner(OctetSequenceKey.parse(accessTokenKey));
    }

    @Override
    public String apply(Token token) {
        var jwsHeader = new JWSHeader.Builder(this.jwsAlgorithm)
                .keyID(token.uuid().toString())
                .build();
        var claimsSet = new JWTClaimsSet.Builder()
                .jwtID(token.uuid().toString())
                .subject(token.subject())
                .issueTime(Date.from(token.createdAt()))
                .expirationTime(Date.from(token.expiresAt()))
                .claim("authorities", token.authorities())
                .build();
        var signedJWT = new SignedJWT(jwsHeader, claimsSet);
        try {
            signedJWT.sign(this.jwsSigner);

            return signedJWT.serialize();
        } catch (JOSEException exception) {
            log.error(exception.getMessage(), exception);
        }

        return null;
    }
}
