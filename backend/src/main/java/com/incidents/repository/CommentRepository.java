package com.incidents.repository;

import com.incidents.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CommentRepository extends JpaRepository<Comment, UUID> {
    Page<Comment> findByIncidentId(UUID incidentId, Pageable pageable);
    List<Comment> findByIncidentId(UUID incidentId);
    void deleteByIncidentId(UUID incidentId);
}
