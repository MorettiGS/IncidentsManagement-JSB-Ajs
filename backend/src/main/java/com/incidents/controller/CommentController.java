package com.incidents.controller;

import com.incidents.model.Comment;
import com.incidents.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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
        return ResponseEntity.ok(commentService.getCommentsByIncidentId(incidentId, pageable));
    }

    @PostMapping
    @Operation(summary = "Add a comment to an incident")
    public ResponseEntity<Comment> addComment(@PathVariable UUID incidentId, @RequestBody Comment comment) {
        comment.setIncidentId(incidentId);
        return ResponseEntity.ok(commentService.createComment(comment));
    }
}