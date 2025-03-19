package com.zubrilka.VideoManager.models.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.zubrilka.VideoManager.enums.Language;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.io.IOException;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Converter(autoApply = true)
public class TranslationConverter implements AttributeConverter<EnumMap<Language, List<String>>, String> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(EnumMap<Language, List<String>> attribute) {
        if (attribute == null) {
            return null;
        }
        try {
            // Сериализуем EnumMap как Map<String, List<String>>
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting EnumMap to JSON", e);
        }
    }

    @Override
    public EnumMap<Language, List<String>> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }
        try {
            // Десериализуем в обычный Map<String, List<String>>
            Map<String, List<String>> tempMap = objectMapper.readValue(dbData, new TypeReference<Map<String, List<String>>>() {});

            // Преобразуем Map<String, List<String>> обратно в EnumMap<Language, List<String>>
            EnumMap<Language, List<String>> enumMap = new EnumMap<>(Language.class);
            for (Map.Entry<String, List<String>> entry : tempMap.entrySet()) {
                enumMap.put(Language.valueOf(entry.getKey()), entry.getValue());
            }
            return enumMap;
        } catch (IOException e) {
            throw new RuntimeException("Error converting JSON to EnumMap", e);
        }
    }
}
