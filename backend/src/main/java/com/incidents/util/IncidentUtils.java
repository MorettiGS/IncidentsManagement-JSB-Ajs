package com.incidents.util;

import com.incidents.model.Incident;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class IncidentUtils {
    
    public static List<String> normalizeTags(List<String> tags) {
        if (tags == null) {
            return List.of();
        }
        
        return tags.stream()
                .filter(tag -> tag != null && !tag.trim().isEmpty())
                .map(String::trim)
                .map(String::toLowerCase)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }
    
    public static void touchUpdate(Incident incident) {
        incident.setDataAtualizacao(java.time.LocalDateTime.now());
    }
}
