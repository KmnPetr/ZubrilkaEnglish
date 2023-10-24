package com.example.bookAnalyzer.models;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "properties")
@Getter
@Setter
public class PropModel {
    @Id
    @Column(name = "key")
    private String key;
    @Column(name = "value")
    private String value;

    public PropModel() {}
}
