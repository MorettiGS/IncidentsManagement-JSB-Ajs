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

    @Column(name = "incident_id", nullable = false)
    private UUID incidentId;

    @NotBlank
    @Size(max = 120)
    private String autor;

    @NotBlank
    @Size(max = 2000)
    private String mensagem;

    @CreationTimestamp
    private LocalDateTime dataCriacao;

    public Comment() {}

	public Comment(UUID id,
                   Incident incident,
                   String autor,
                   String mensagem,
                   LocalDateTime dataCriacao) {
        this.id = id;
        this.incident = incident;
        this.autor = autor;
        this.mensagem = mensagem;
        this.dataCriacao = dataCriacao;
    }

	public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getIncidentId() {
        return incidentId;
    }
    
    public void setIncidentId(UUID incidentId) {
        this.incidentId = incidentId;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
}
