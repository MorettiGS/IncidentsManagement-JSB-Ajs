package com.incidents.repository;

import com.incidents.model.Incident;
import com.incidents.model.Prioridade;
import com.incidents.model.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, UUID>, JpaSpecificationExecutor<Incident> {
    Page<Incident> findByStatus(Status status, Pageable pageable);
    Page<Incident> findByPrioridade(Prioridade prioridade, Pageable pageable);
    Page<Incident> findByResponsavelEmail(String email, Pageable pageable);
    
    @Query("SELECT i FROM Incident i WHERE " +
           "LOWER(i.titulo) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(i.descricao) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Incident> search(@Param("search") String search, Pageable pageable);
    
    @Query("SELECT i.status as status, COUNT(i) as count FROM Incident i GROUP BY i.status")
    List<Object[]> countByStatus();
    
    @Query("SELECT i.prioridade as prioridade, COUNT(i) as count FROM Incident i GROUP BY i.prioridade")
    List<Object[]> countByPrioridade();
}
