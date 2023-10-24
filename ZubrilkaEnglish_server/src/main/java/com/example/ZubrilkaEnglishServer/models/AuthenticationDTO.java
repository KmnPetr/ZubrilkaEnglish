package com.example.ZubrilkaEnglishServer.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public class AuthenticationDTO {
    @Email(message = "Email should be valid")
    @NotEmpty(message = "Email should not be empty.")
    private String email;//в качестве имени пользователя будет емаил
    private String password;

    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}
    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}
}
