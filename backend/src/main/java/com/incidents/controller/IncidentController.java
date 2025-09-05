package com.incidents.controller;

import io.swagger.v3.oas.annotations.Operation;
import com.incidents.model.Incident;
import com.incidents.model.Status;
import com.incidents.service.IncidentService;

import java.util.Map;
import java.util.UUID;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
@RestController
@RequestMapping("/api/incidents")
public class IncidentController {
    private final IncidentService incidentService;

    public IncidentController(IncidentService incidentService) {
        this.incidentService = incidentService;
    }

    @GetMapping
    @Operation(summary = "Get paginated incidents with filters")
    public ResponseEntity<Page<Incident>> getIncidents(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String prioridade,
            @RequestParam(required = false) String search,
            @ParameterObject Pageable pageable) {
        log.info("GET /api/incidents - status={} prioridade={} search={} pageable={}", status, prioridade, search, pageable);
        return ResponseEntity.ok(incidentService.getIncidents(status, prioridade, search, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get incident by ID")
    public ResponseEntity<?> getIncident(@PathVariable UUID id) {
        log.info("GET /api/incidents/{} - retrieving", id);
        try {
            Incident inc = incidentService.getIncident(id);
            return ResponseEntity.ok(inc);
        } catch (Exception e) {
            log.error("GET /api/incidents/{} - error: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Failed to load incident", "details", e.getMessage()));
        }
    }

    @PostMapping
    @Operation(summary = "Create a new incident")
    public ResponseEntity<?> createIncident(@RequestBody Incident incident) {
        log.info("POST /api/incidents - create attempt: title='{}' responsible='{}'", incident.getTitulo(), incident.getResponsavelEmail());
        try {
            if (incident.getTags() == null) incident.setTags("");
            Incident created = incidentService.createIncident(incident);
            log.info("POST /api/incidents - created id={}", created.getId());
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            log.error("POST /api/incidents - error creating incident: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Failed to create incident", "details", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing incident")
    public ResponseEntity<?> updateIncident(@PathVariable UUID id, @RequestBody Incident incident) {
        log.info("PUT /api/incidents/{} - update attempt", id);
        try {
            if (incident.getTags() == null) incident.setTags("");
            Incident updated = incidentService.updateIncident(id, incident);
            log.info("PUT /api/incidents/{} - updated", id);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            log.error("PUT /api/incidents/{} - error updating: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Failed to update incident", "details", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an incident")
    public ResponseEntity<?> deleteIncident(@PathVariable UUID id) {
        log.info("DELETE /api/incidents/{} - deleting", id);
        try {
            incidentService.deleteIncident(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("DELETE /api/incidents/{} - error: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Failed to delete incident", "details", e.getMessage()));
        }
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update incident status")
    public ResponseEntity<?> updateStatus(@PathVariable UUID id, @RequestBody StatusUpdateRequest request) {
        log.info("PATCH /api/incidents/{}/status - newStatus={}", id, request.getStatus());
        try {
            Incident changed = incidentService.updateStatus(id, request.getStatus());
            return ResponseEntity.ok(changed);
        } catch (Exception e) {
            log.error("PATCH /api/incidents/{}/status - error updating status: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Failed to update status", "details", e.getMessage()));
        }
    }

    // DTO
    public static class StatusUpdateRequest {
        private Status status;

        public Status getStatus() {
            return status;
        }

        public void setStatus(Status status) {
            this.status = status;
        }
    }
}
