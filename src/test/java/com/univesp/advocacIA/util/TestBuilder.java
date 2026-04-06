package com.univesp.advocacIA.util;

import com.univesp.advocacIA.model.Usuario;
import com.univesp.advocacIA.model.UsuarioLogin;
import com.univesp.advocacIA.model.Questao;

public class TestBuilder {

    public static Usuario criarUsuario(Long id, String nome, String usuario, String senha, int pontos) {
        Usuario novoUsuario = new Usuario();
        novoUsuario.setId(id);
        novoUsuario.setNome(nome);
        novoUsuario.setUsuario(usuario);
        novoUsuario.setSenha(senha);
        novoUsuario.setPontos(pontos);
        return novoUsuario;
    }

    public static UsuarioLogin criarUsuarioLogin(String usuario, String senha) {
        UsuarioLogin usuarioLogin = new UsuarioLogin();
        usuarioLogin.setUsuario(usuario);
        usuarioLogin.setSenha(senha);
        return usuarioLogin;
    }

    public static Questao criarQuestao(
            Long id,
            String enunciado,
            String alternativaA,
            String alternativaB,
            String alternativaC,
            String alternativaD,
            String alternativaE,
            Questao.Resposta resposta,
            String assunto) {

        Questao novaQuestao = new Questao();
        novaQuestao.setId(id);
        novaQuestao.setEnunciado(enunciado);
        novaQuestao.setAlternativaA(alternativaA);
        novaQuestao.setAlternativaB(alternativaB);
        novaQuestao.setAlternativaC(alternativaC);
        novaQuestao.setAlternativaD(alternativaD);
        novaQuestao.setAlternativaE(alternativaE);
        novaQuestao.setResposta(resposta);
        novaQuestao.setAssunto(assunto);
        novaQuestao.setFaseOAB(Questao.FaseOAB.PRIMEIRA_FASE);
        novaQuestao.setDificuldade(Questao.Dificuldade.MEDIO);
        novaQuestao.setTipoQuestao(Questao.TipoQuestao.TEORICA);

        return novaQuestao;
    }

}
