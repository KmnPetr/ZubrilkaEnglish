package com.example.zeapp.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

@Table("person")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Person implements UserDetails {
    @Id
    @Column("id")
    private Long id;
    @Column("email")
    private String email;
    @JsonIgnore
    @Column("password")
    private String password;
    @Column("short_name")
    private String short_name;
    @Column("role")
    private UserRole role;
    @Column("created_at")
    private Timestamp created_at;
    @Column("is_temp_prof")
    private Boolean isTempProf;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
