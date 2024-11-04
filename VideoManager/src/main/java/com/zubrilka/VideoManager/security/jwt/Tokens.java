package com.zubrilka.VideoManager.security.jwt;

/**
 * the class is returned to the user, for example, in json format
 * contains access tokens and their expiration time
 */
public record Tokens(String accessToken, String accessTokenExpiry,
                     String refreshToken, String refreshTokenExpiry) {
}
