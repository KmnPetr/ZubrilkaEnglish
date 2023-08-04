package com.example.ZubrilkaEnglishServer.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "properties")
public class PropModel {
    @Id
    @Column(name = "key")
    private String key;
    @Column(name = "value")
    private String value;

    public PropModel() {}

    public String getKey() {return key;}
    public void setKey(String key) {this.key = key;}
    public String getValue() {return value;}
    public void setValue(String value) {this.value = value;}
}
