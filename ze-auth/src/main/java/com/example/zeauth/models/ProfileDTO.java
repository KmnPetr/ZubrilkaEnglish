package com.example.zeauth.models;

import jakarta.validation.constraints.*;
import lombok.*;

/**
 * обьект дляпередачи данных пользователя по сети
 */
@Data
@ToString
@AllArgsConstructor
public class ProfileDTO {
        @NotBlank(message = "Email is blank.")
        @Email(message = "Email is invalid.")
        @Size(max = 100, message = "The email is too long.")
    private String email;
        @NotBlank(message = "RequestPassword is blank.")
        @Size(min = 7,message = "The password is shorter than 7 characters.")
        @Size(max = 100, message = "The password is too long.")
    private String requestPassword;
        @NotBlank(message = "Name is blank.")
        @Size(max = 100, message = "The name is too long.")
    private String name;
}