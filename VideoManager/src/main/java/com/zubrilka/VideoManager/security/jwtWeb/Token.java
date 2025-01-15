package com.zubrilka.VideoManager.security.jwtWeb;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class Token{
    public UUID id;
    public String subject;
    public List<String> authorities;
    public Instant createdAt;
    public Instant expiresAt;

    public Token(UUID id, String subject, List<String> authorities, Instant createdAt, Instant expiresAt) {
        this.id = id;
        this.subject = subject;
        this.authorities = authorities;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }
}