package com.univesp.advocacIA.model;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("MULTIPLA_ESCOLHA")
public class QuestaoMultiplaEscolha extends QuestaoBase {

    @Column(length = 1000)
    private String alternativaA;

    @Column(length = 1000)
    private String alternativaB;

    @Column(length = 1000)
    private String alternativaC;

    @Column(length = 1000)
    private String alternativaD;

    @Enumerated(EnumType.STRING)
    private Resposta resposta;

    public QuestaoMultiplaEscolha() {
        this.setTipoQuestao(TipoQuestao.MULTIPLA_ESCOLHA);
    }

    public enum Resposta {
        A, B, C, D
    }

    // Getters and Setters
    public String getAlternativaA() {
        return alternativaA;
    }

    public void setAlternativaA(String alternativaA) {
        this.alternativaA = alternativaA;
    }

    public String getAlternativaB() {
        return alternativaB;
    }

    public void setAlternativaB(String alternativaB) {
        this.alternativaB = alternativaB;
    }

    public String getAlternativaC() {
        return alternativaC;
    }

    public void setAlternativaC(String alternativaC) {
        this.alternativaC = alternativaC;
    }

    public String getAlternativaD() {
        return alternativaD;
    }

    public void setAlternativaD(String alternativaD) {
        this.alternativaD = alternativaD;
    }

    public Resposta getResposta() {
        return resposta;
    }

    public void setResposta(Resposta resposta) {
        this.resposta = resposta;
    }
}
