package com.incidents.service;

import com.incidents.model.Incident;
import com.incidents.model.Status;
import com.incidents.repository.IncidentRepository;
import com.incidents.util.IncidentUtils;
import com.incidents.util.QueryBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class IncidentServiceImpl implements IncidentService {
    private final IncidentRepository incidentRepository;
    
    public IncidentServiceImpl(IncidentRepository incidentRepository) {
        this.incidentRepository = incidentRepository;
    }
    
    @Override
    public Page<Incident> getIncidents(String status, String prioridade, String search, Pageable pageable) {
        Specification<Incident> spec = QueryBuilder.buildIncidentFilter(status, prioridade, search);
        return incidentRepository.findAll(spec, pageable);
    }
    
    @Override
    public Incident getIncident(UUID id) {
        return incidentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Incident not found with id: " + id));
    }
    
    @Override
    @Transactional
    public Incident createIncident(Incident incident) {
        incident.setTags(IncidentUtils.normalizeTags(incident.getTags()));
        
        incident.setDataAbertura(java.time.LocalDateTime.now());
        incident.setDataAtualizacao(java.time.LocalDateTime.now());
        
        return incidentRepository.save(incident);
    }
    
    @Override
    @Transactional
    public Incident updateIncident(UUID id, Incident incidentDetails) {
        Incident incident = getIncident(id);
        
        incident.setTitulo(incidentDetails.getTitulo());
        incident.setDescricao(incidentDetails.getDescricao());
        incident.setPrioridade(incidentDetails.getPrioridade());
        incident.setStatus(incidentDetails.getStatus());
        incident.setResponsavelEmail(incidentDetails.getResponsavelEmail());
        
        incident.setTags(IncidentUtils.normalizeTags(incidentDetails.getTags()));
        IncidentUtils.touchUpdate(incident);
        
        return incidentRepository.save(incident);
    }
    
    @Override
    @Transactional
    public void deleteIncident(UUID id) {
        Incident incident = getIncident(id);
        incidentRepository.delete(incident);
    }
    
    @Override
    @Transactional
    public Incident updateStatus(UUID id, Status status) {
        Incident incident = getIncident(id);
        incident.setStatus(status);
        IncidentUtils.touchUpdate(incident);
        return incidentRepository.save(incident);
    }
    
    @Override
    public List<Object[]> getStatsByStatus() {
        return incidentRepository.countByStatus();
    }
    
    @Override
    public List<Object[]> getStatsByPrioridade() {
        return incidentRepository.countByPrioridade();
    }
}
