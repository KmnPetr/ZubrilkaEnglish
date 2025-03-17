package com.zubrilka.VideoManager.security;


import com.zubrilka.VideoManager.models.Person;
import com.zubrilka.VideoManager.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.sql.Timestamp;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtUtilTest {

    private Person testPerson;
    private JwtUtil jwtUtil;
    private UUID uuid;

    @BeforeEach
    void setUp() {
        uuid = UUID.randomUUID();

        testPerson = new Person(
                uuid,
                "password",
                "username",
                UserRole.ROLE_TRANSLATOR,
                new Timestamp(System.currentTimeMillis()),
                null
        );
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "secret", "TestSecret");
        ReflectionTestUtils.setField(jwtUtil, "timeExpirationAccess", "3600");
        ReflectionTestUtils.setField(jwtUtil, "timeExpirationRefresh", "7200");
    }

    @Test
    void validateToken() {
    }

    @Test
    void getClaimsFromToken() {
    }

    @Test
    void generateAccessToken() {

        String token = jwtUtil.generateAccessToken(testPerson);

        assertTrue(jwtUtil.validateToken(token));

        assertEquals(jwtUtil.getUserIdFromToken(token),testPerson.getUuid());
        assertEquals(jwtUtil.getClaimsFromToken(token).get("role"),testPerson.getRole().toString());
    }

    @Test
    void generateRefreshToken() {
    }

    @Test
    void getUserIdFromToken() {
    }
}