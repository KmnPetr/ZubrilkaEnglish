package com.example.ZubrilkaEnglishServer.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class PersonDTO {
    @Email(message = "Email should be valid")
    @NotEmpty(message = "Email should not be empty.")
    private String email;
    private String password;
    @Size(min = 2,max = 100,message = "имя должно быть от 2 до 100 символов длиной")
    private String shortName;

    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}
    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}
    public String getShortName() {return shortName;}
    public void setShortName(String shortName) {this.shortName = shortName;}
}
