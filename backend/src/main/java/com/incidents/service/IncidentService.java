package com.incidents.service;

import com.incidents.model.Incident;
import com.incidents.model.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface IncidentService {
    Page<Incident> getIncidents(String status, String prioridade, String search, Pageable pageable);
    Incident getIncident(UUID id);
    Incident createIncident(Incident incident);
    Incident updateIncident(UUID id, Incident incident);
    void deleteIncident(UUID id);
    Incident updateStatus(UUID id, Status status);
    List<Object[]> getStatsByStatus();
    List<Object[]> getStatsByPrioridade();
}
