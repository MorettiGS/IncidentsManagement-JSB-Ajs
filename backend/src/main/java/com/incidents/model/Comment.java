package com.incidents.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "incident_id", nullable = false)
    private Incident incident;

    @NotBlank
    @Size(max = 120)
    private String autor;

    @NotBlank
    @Size(max = 2000)
    private String mensagem;

    @CreationTimestamp
    private LocalDateTime dataCriacao;

    public Comment() {}
}
