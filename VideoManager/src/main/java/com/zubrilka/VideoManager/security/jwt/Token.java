package com.zubrilka.VideoManager.security.jwt;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * a class for storing access and refresh tokens
 */
public record Token(UUID uuid,
                    String subject,
                    List<String> authorities,
                    Instant createdAt,
                    Instant expiresAt) {
}
