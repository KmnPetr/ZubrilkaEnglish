package com.zubrilka.VideoManager.models.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zubrilka.VideoManager.models.Phrase;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.List;

/**
 * The converter converts the phrase list field in the translation class to a text format and in reverse
 */
@Converter(autoApply = false)
public class PhraseListConverter implements AttributeConverter<List<Phrase>, String> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<Phrase> phrases) {
        if (phrases == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(phrases);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting list to JSON", e);
        }
    }

    @Override
    public List<Phrase> convertToEntityAttribute(String json) {
        if (json == null) {
            return null;
        }
        try {
            return objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, Phrase.class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting JSON to list", e);
        }
    }
}
