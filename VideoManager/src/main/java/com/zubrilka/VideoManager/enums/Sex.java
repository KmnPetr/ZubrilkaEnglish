package com.zubrilka.VideoManager.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum Sex {
    MALE("male"),
    FEMALE("female");

    private final String value;

    Sex(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static Sex fromValue(String value) {
        return Arrays.stream(Sex.values())
                .filter(sex -> sex.value.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown sex: " + value));
    }
}
