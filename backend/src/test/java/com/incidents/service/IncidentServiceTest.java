package com.incidents.service;

import com.incidents.model.Incident;
import com.incidents.model.Prioridade;
import com.incidents.model.Status;
import com.incidents.repository.IncidentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IncidentServiceTest {
    
    @Mock
    private IncidentRepository incidentRepository;
    
    @InjectMocks
    private IncidentServiceImpl incidentService;
    
    @Test
    void getIncidents_WithPagination_ReturnsPaginatedResults() {
        Pageable pageable = PageRequest.of(0, 5);
        List<Incident> incidents = List.of(
            createTestIncident("Issue 1"),
            createTestIncident("Issue 2")
        );
        Page<Incident> expectedPage = new PageImpl<>(incidents, pageable, incidents.size());
        
        when(incidentRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);
        
        Page<Incident> result = incidentService.getIncidents(null, null, null, pageable);
        
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(5, result.getSize());
        assertEquals(0, result.getNumber());
    }
    
    private Incident createTestIncident(String title) {
        Incident incident = new Incident();
        incident.setId(UUID.randomUUID());
        incident.setTitulo(title);
        incident.setPrioridade(Prioridade.MEDIA);
        incident.setStatus(Status.ABERTA);
        incident.setResponsavelEmail("test@example.com");
        return incident;
    }
}
