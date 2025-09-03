package com.incidents.service;

import com.incidents.model.Comment;
import com.incidents.repository.CommentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Page<Comment> getCommentsByIncidentId(UUID incidentId, Pageable pageable) {
        return commentRepository.findByIncidentId(incidentId, pageable);
    }

    public List<Comment> getCommentsByIncidentId(UUID incidentId) {
        return commentRepository.findByIncidentId(incidentId);
    }

    @Transactional
    public Comment createComment(Comment comment) {
        return commentRepository.save(comment);
    }

    @Transactional
    public void deleteCommentsByIncidentId(UUID incidentId) {
        commentRepository.deleteByIncidentId(incidentId);
    }
}