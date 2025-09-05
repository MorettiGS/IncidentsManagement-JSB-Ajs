package com.incidents.controller;

import com.incidents.model.Comment;
import com.incidents.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/incidents/{incidentId}/comments")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    @Operation(summary = "Get comments for an incident")
    public ResponseEntity<Page<Comment>> getComments(@PathVariable UUID incidentId, Pageable pageable) {
        log.info("GET /api/incidents/{}/comments - pageable={}", incidentId, pageable);
        return ResponseEntity.ok(commentService.getCommentsByIncidentId(incidentId, pageable));
    }

    @PostMapping
    @Operation(summary = "Add a comment to an incident")
    public ResponseEntity<Comment> addComment(@PathVariable UUID incidentId, @RequestBody Comment comment) {
        log.info("POST /api/incidents/{}/comments - adding by author={}", incidentId, comment.getAutor());
        comment.setIncidentId(incidentId);
        return ResponseEntity.ok(commentService.createComment(comment));
    }
}