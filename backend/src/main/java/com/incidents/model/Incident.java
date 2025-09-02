package com.incidents.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "incident")
public class Incident {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    @Size(min = 5, max = 120)
    private String titulo;

    @Size(max = 5000)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Prioridade prioridade;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Status status;

    @Email
    @NotBlank
    private String responsavelEmail;

    @ElementCollection
    private List<String> tags;

    @CreationTimestamp
    private LocalDateTime dataAbertura;

    @UpdateTimestamp
    private LocalDateTime dataAtualizacao;

    public Incident() {}
}
