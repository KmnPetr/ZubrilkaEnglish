package com.zubrilka.VideoManager.security;

import com.zubrilka.VideoManager.models.Person;
import com.zubrilka.VideoManager.models.UserRole;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.token.TokenService;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

class JwtUtilTest {

    private static Person testPerson;

    @BeforeEach
    void setUp() {
        testPerson = new Person(
                1L,
                "test@t.t",
                "password",
                "short_name",
                UserRole.ROLE_USER,
                new Timestamp(System.currentTimeMillis())
        );
    }


    @Test
    void validateToken() {
    }

    @Test
    void getClaimsFromToken() {
    }

    @Test
    void generateAccessToken() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
//        // Создаем spy объект JwtUtil
//        JwtUtil jwtUtil = Mockito.spy(new JwtUtil());
//
//        // Установка значения переменной timeExpirationAccess через рефлексию
//        Field timeExpirationAccessField = JwtUtil.class.getDeclaredField("timeExpirationAccess");
//        timeExpirationAccessField.setAccessible(true);
//        timeExpirationAccessField.set(jwtUtil, "3600");
//
//        // Параметр времени истечения токена (можно заменить на реальный или замокировать)
//        String timeExpirationAccess = "10045634624";
//
//        // Ожидаемое значение токена
//        String expectedToken = "mocked_token";
//
//        // Получение приватного метода generateToken с помощью рефлексии
//        Method privateGenerateToken = JwtUtil.class.getDeclaredMethod("generateToken", Person.class, String.class);
//        privateGenerateToken.setAccessible(true);
//
//        // Мокирование результата приватного метода через рефлексию
//        String mockedToken = (String) privateGenerateToken.invoke(jwtUtil, testPerson, timeExpirationAccess);
//
//        // Вызов публичного метода generateAccessToken
//        String actualToken = jwtUtil.generateAccessToken(testPerson);
//
//        // Проверка того, что токены совпадают
//        assertEquals(mockedToken, actualToken);

        //TODO там короче при использовании приватного метода generateToken каждый раз устанавливается новое значение времени и токен получается разный каждый раз
    }

    @Test
    void generateRefreshToken() {
    }

    @Test
    void getUserIdFromToken() {
    }
}