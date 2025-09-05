package com.incidents.service;

import com.incidents.model.Incident;
import com.incidents.model.Status;
import com.incidents.repository.IncidentRepository;
import com.incidents.util.IncidentUtils;
import com.incidents.util.QueryBuilder;
import com.incidents.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;

import java.util.List;
import java.util.UUID;

@Service
public class IncidentServiceImpl implements IncidentService {
    private final IncidentRepository incidentRepository;

    public IncidentServiceImpl(IncidentRepository incidentRepository) {
        this.incidentRepository = incidentRepository;
    }

    @Override
    @Cacheable(value = "incidents", key = "{#status, #prioridade, #search, #pageable.pageNumber, #pageable.pageSize, #pageable.sort}")
    public Page<Incident> getIncidents(String status, String prioridade, String search, Pageable pageable) {
        Specification<Incident> spec = QueryBuilder.buildIncidentFilter(status, prioridade, search);
        return incidentRepository.findAll(spec, pageable);
    }

    @Override
    @Cacheable(value = "incidentById", key = "#id")
    public Incident getIncident(UUID id) {
        return incidentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Incident not found with id: " + id));
    }

    @Override
    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "incidents", allEntries = true),
        @CacheEvict(value = "incidentById", key = "#result.id"),
        @CacheEvict(value = "statsByStatus", allEntries = true),
        @CacheEvict(value = "statsByPrioridade", allEntries = true),
        @CacheEvict(value = "commentsByIncident", allEntries = true)
    })
    public Incident createIncident(Incident incident) {
        incident.setTags(IncidentUtils.normalizeTagsToJson(incident.getTagsList()));

        incident.setDataAbertura(java.time.LocalDateTime.now());
        incident.setDataAtualizacao(java.time.LocalDateTime.now());

        return incidentRepository.save(incident);
    }

    @Override
    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "incidents", allEntries = true),
        @CacheEvict(value = "incidentById", key = "#id"),
        @CacheEvict(value = "statsByStatus", allEntries = true),
        @CacheEvict(value = "statsByPrioridade", allEntries = true)
    })
    public Incident updateIncident(UUID id, Incident incidentDetails) {
        Incident incident = getIncident(id);

        incident.setTitulo(incidentDetails.getTitulo());
        incident.setDescricao(incidentDetails.getDescricao());
        incident.setPrioridade(incidentDetails.getPrioridade());
        incident.setStatus(incidentDetails.getStatus());
        incident.setResponsavelEmail(incidentDetails.getResponsavelEmail());

        incident.setTags(IncidentUtils.normalizeTagsToJson(incidentDetails.getTagsList()));
        IncidentUtils.touchUpdate(incident);

        return incidentRepository.save(incident);
    }

    @Override
    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "incidents", allEntries = true),
        @CacheEvict(value = "incidentById", key = "#id"),
        @CacheEvict(value = "commentsByIncident", key = "#id"),
        @CacheEvict(value = "statsByStatus", allEntries = true),
        @CacheEvict(value = "statsByPrioridade", allEntries = true)
    })
    public void deleteIncident(UUID id) {
        Incident incident = getIncident(id);
        incidentRepository.delete(incident);
    }

    @Override
    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "incidents", allEntries = true),
        @CacheEvict(value = "incidentById", key = "#id"),
        @CacheEvict(value = "statsByStatus", allEntries = true),
        @CacheEvict(value = "statsByPrioridade", allEntries = true)
    })
    public Incident updateStatus(UUID id, Status status) {
        Incident incident = getIncident(id);
        incident.setStatus(status);
        IncidentUtils.touchUpdate(incident);
        return incidentRepository.save(incident);
    }

    @Override
    @Cacheable(value = "statsByStatus")
    public List<Object[]> getStatsByStatus() {
        return incidentRepository.countByStatus();
    }

    @Override
    @Cacheable(value = "statsByPrioridade")
    public List<Object[]> getStatsByPrioridade() {
        return incidentRepository.countByPrioridade();
    }
}
