package com.univesp.advocacIA.model;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("DISSERTATIVA")
public class QuestaoDissertativa extends QuestaoBase {

    @Lob
    @Column(columnDefinition = "TEXT")
    private String respostaEsperada;

    // Getters and Setters
    public String getRespostaEsperada() {
        return respostaEsperada;
    }

    public void setRespostaEsperada(String respostaEsperada) {
        this.respostaEsperada = respostaEsperada;
    }
}
