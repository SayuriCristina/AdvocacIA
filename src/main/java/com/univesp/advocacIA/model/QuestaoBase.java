package com.univesp.advocacIA.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes;

@Entity
@Table(name = "tb_questoes")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_questao_discriminator", discriminatorType = DiscriminatorType.STRING)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = QuestaoMultiplaEscolha.class, name = "MULTIPLA_ESCOLHA"),
        @JsonSubTypes.Type(value = QuestaoDissertativa.class, name = "DISSERTATIVA")
})
public abstract class QuestaoBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(columnDefinition = "TEXT")
    @NotBlank(message = "O enunciado não pode estar vazio")
    @Size(min = 5, message = "O enunciado deve ter pelo menos 5 caracteres")
    private String enunciado;

    private String assunto;

    @Enumerated(EnumType.STRING)
    private FaseOAB faseOAB;

    @Enumerated(EnumType.STRING)
    private Dificuldade dificuldade;

    public enum FaseOAB {
        PRIMEIRA_FASE, SEGUNDA_FASE
    }

    public enum Dificuldade {
        FACIL, MEDIO, DIFICIL
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEnunciado() {
        return enunciado;
    }

    public void setEnunciado(String enunciado) {
        this.enunciado = enunciado;
    }

    public String getAssunto() {
        return assunto;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    public FaseOAB getFaseOAB() {
        return faseOAB;
    }

    public void setFaseOAB(FaseOAB faseOAB) {
        this.faseOAB = faseOAB;
    }

    public Dificuldade getDificuldade() {
        return dificuldade;
    }

    public void setDificuldade(Dificuldade dificuldade) {
        this.dificuldade = dificuldade;
    }

}
