package com.example.ZubrilkaEnglishServer.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
@Table(name = "Person")
public class Person {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "email")
    @Email(message = "Email should be valid")
    @NotEmpty(message = "Email should not be empty.")
    private String email;
    @Column(name = "password")
    private String password;
    @Size(min = 2,max = 100,message = "имя должно быть от 2 до 100 символов длиной")
    @Column(name = "short_name")
    private String shortName;
    @Column(name = "role")
    private String role;
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Person() {}

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}
    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}
    public String getShortName() {return shortName;}
    public void setShortName(String shortName) {this.shortName = shortName;}
    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}
    public String getRole() {return role;}
    public void setRole(String role) {this.role = role;}
    public LocalDateTime getCreatedAt() {return createdAt;}
    public void setCreatedAt(LocalDateTime createdAt) {this.createdAt = createdAt;}
}
