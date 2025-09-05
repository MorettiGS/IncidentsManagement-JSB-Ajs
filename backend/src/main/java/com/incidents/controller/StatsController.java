package com.incidents.controller;

import com.incidents.service.IncidentService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/stats")
public class StatsController {
    private final IncidentService incidentService;

    public StatsController(IncidentService incidentService) {
        this.incidentService = incidentService;
    }

    @GetMapping("/incidents")
    @Operation(summary = "Get incident statistics")
    public ResponseEntity<?> getStats() {
        log.info("GET /api/stats/incidents - computing stats");

        try {
            List<Object[]> statusStats = Optional.ofNullable(incidentService.getStatsByStatus()).orElse(Collections.emptyList());
            List<Object[]> priorityStats = Optional.ofNullable(incidentService.getStatsByPrioridade()).orElse(Collections.emptyList());

            Map<String, Long> statusMap = rowsToMap(statusStats);
            Map<String, Long> priorityMap = rowsToMap(priorityStats);

            Map<String, Object> payload = new LinkedHashMap<>();
            payload.put("byStatus", statusMap);
            payload.put("byPriority", priorityMap);
            payload.put("total", statusMap.values().stream().mapToLong(Long::longValue).sum());

            return ResponseEntity.ok(payload);
        } catch (Exception e) {
            log.error("GET /api/stats/incidents - unexpected error computing stats", e);
            Map<String, Object> err = new LinkedHashMap<>();
            err.put("message", "An unexpected error occurred while computing stats");
            err.put("details", e.getMessage());
            return ResponseEntity.status(500).body(err);
        }
    }

    private Map<String, Long> rowsToMap(List<Object[]> rows) {
        Map<String, Long> map = new LinkedHashMap<>();
        if (rows == null) return map;
        for (Object[] row : rows) {
            try {
                if (row == null || row.length < 2) continue;
                String key = String.valueOf(row[0]);
                Object rawValue = row[1];
                long value;
                if (rawValue instanceof Number) {
                    value = ((Number) rawValue).longValue();
                } else {
                    value = Long.parseLong(String.valueOf(rawValue));
                }
                map.put(key, value);
            } catch (Exception ex) {
                log.warn("Failed to parse stats row {} : {}", Arrays.toString(row), ex.toString());
                String key = row != null && row.length>0 ? String.valueOf(row[0]) : "unknown";
                map.putIfAbsent(key, 0L);
            }
        }
        return map;
    }
}
