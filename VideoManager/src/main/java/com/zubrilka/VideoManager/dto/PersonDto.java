package com.zubrilka.VideoManager.dto;

import com.zubrilka.VideoManager.enums.Sex;
import com.zubrilka.VideoManager.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.UUID;

/**
 * некоторая информация для передачи по сети данных пользователя токенов доступа и др. кроме пароля
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PersonDto {
    private UUID uuid;
    private String username;
    private String short_name;
    private Sex sex;
    private UserRole role;
    private Timestamp created_at;
    private String accessToken;
    private String refreshToken;
}
