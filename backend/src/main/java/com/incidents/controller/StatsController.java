package com.incidents.controller;

import com.incidents.service.IncidentService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/stats")
public class StatsController {
    private final IncidentService incidentService;

    public StatsController(IncidentService incidentService) {
        this.incidentService = incidentService;
    }

    @GetMapping("/incidents")
    @Operation(summary = "Get incident statistics")
    public ResponseEntity<Map<String, Object>> getStats() {
        List<Object[]> statusStats = incidentService.getStatsByStatus();
        List<Object[]> priorityStats = incidentService.getStatsByPrioridade();

        Map<String, Long> statusMap = statusStats.stream()
                .collect(Collectors.toMap(
                        arr -> (String) arr[0],
                        arr -> (Long) arr[1]
                ));

        Map<String, Long> priorityMap = priorityStats.stream()
                .collect(Collectors.toMap(
                        arr -> (String) arr[0],
                        arr -> (Long) arr[1]
                ));

        return ResponseEntity.ok(Map.of(
                "byStatus", statusMap,
                "byPriority", priorityMap
        ));
    }
}