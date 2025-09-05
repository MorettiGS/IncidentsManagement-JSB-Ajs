package com.incidents.util;

import com.incidents.model.Incident;
import com.incidents.model.Prioridade;
import com.incidents.model.Status;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Component
public class QueryBuilder {
    
    public static Specification<Incident> buildIncidentFilter(String status, String prioridade, String search) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (status != null && !status.isEmpty()) {
                try {
                    Status statusEnum = Status.valueOf(status.toUpperCase());
                    predicates.add(criteriaBuilder.equal(root.get("status"), statusEnum));
                } catch (IllegalArgumentException e) {
                }
            }
            
            if (prioridade != null && !prioridade.isEmpty()) {
                try {
                    Prioridade prioridadeEnum = Prioridade.valueOf(prioridade.toUpperCase());
                    predicates.add(criteriaBuilder.equal(root.get("prioridade"), prioridadeEnum));
                } catch (IllegalArgumentException e) {
                }
            }
            
            if (search != null && !search.isEmpty()) {
                String searchPattern = "%" + search.toLowerCase() + "%";
                predicates.add(criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("titulo")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("descricao")), searchPattern)
                ));
            }
            
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
