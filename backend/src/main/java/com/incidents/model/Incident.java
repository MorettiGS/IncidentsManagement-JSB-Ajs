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

	public Incident(UUID id,
                    String titulo,
                    String descricao,
                    Prioridade prioridade,
                    Status status,
                    String responsavelEmail,
                    List<String> tags,
                    LocalDateTime dataAbertura,
                    LocalDateTime dataAtualizacao) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.prioridade = prioridade;
        this.status = status;
        this.responsavelEmail = responsavelEmail;
        this.tags = tags;
        this.dataAbertura = dataAbertura;
        this.dataAtualizacao = dataAtualizacao;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Prioridade getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(Prioridade prioridade) {
        this.prioridade = prioridade;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getResponsavelEmail() {
        return responsavelEmail;
    }

    public void setResponsavelEmail(String responsavelEmail) {
        this.responsavelEmail = responsavelEmail;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public LocalDateTime getDataAbertura() {
        return dataAbertura;
    }

    public void setDataAbertura(LocalDateTime dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
    	this.dataAtualizacao = dataAtualizacao;
	}
}
