package com.incidents.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.incidents.model.Incident;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class IncidentUtils {
    
    public static String normalizeTagsToJson(List<String> tags) {
        if (tags == null || tags.isEmpty()) {
            return "[]";
        }
        
        List<String> normalizedTags = tags.stream()
                .filter(tag -> tag != null && !tag.trim().isEmpty())
                .map(String::trim)
                .map(String::toLowerCase)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
        
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(normalizedTags);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting tags to JSON", e);
        }
    }
    
    public static List<String> parseTagsFromJson(String tagsJson) {
        if (tagsJson == null || tagsJson.trim().isEmpty()) {
            return List.of();
        }
        
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(tagsJson, new TypeReference<List<String>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error parsing tags JSON", e);
        }
    }
    
    public static void touchUpdate(Incident incident) {
        incident.setDataAtualizacao(java.time.LocalDateTime.now());
    }
}