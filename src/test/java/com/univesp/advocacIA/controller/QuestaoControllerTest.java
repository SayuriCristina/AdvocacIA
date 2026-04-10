package com.univesp.advocacIA.controller;

import com.univesp.advocacIA.model.QuestaoBase;
import com.univesp.advocacIA.model.QuestaoMultiplaEscolha;
import com.univesp.advocacIA.repository.QuestaoRepository;
import com.univesp.advocacIA.repository.UsuarioRepository;
import com.univesp.advocacIA.service.UsuarioService;
import com.univesp.advocacIA.util.JwtHelper;
import com.univesp.advocacIA.util.TestBuilder;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.Optional;

import static com.univesp.advocacIA.model.QuestaoMultiplaEscolha.Resposta.A;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.DisplayName.class)
class QuestaoControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private QuestaoRepository questaoRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private static final String BASE_URL = "/questao";
    private static final String ADMIN = "root@root.com";
    private static final String SENHA = "rootroot";
    private String token;

    private static final String ALT_A = "Alternativa A correta";
    private static final String ALT_B = "Alternativa B incorreta";
    private static final String ALT_C = "Alternativa C incorreta";
    private static final String ALT_D = "Alternativa D incorreta";


    @BeforeAll
    void start() {

        questaoRepository.deleteAll();
        usuarioRepository.deleteAll();

        usuarioService.cadastrarUsuario(
                TestBuilder.criarUsuario(null, "Root", ADMIN, SENHA, 0)
        );

        token = JwtHelper.obterToken(testRestTemplate, ADMIN, SENHA);
    }


    @Test
    @DisplayName("✔ 01 - Deve cadastrar uma questão com sucesso")
    void deveCadastrarQuestao() {

        // Given
        QuestaoMultiplaEscolha questao = TestBuilder.criarQuestao(null,
                "Uma questão sobre direito constitucional na OAB?",
                ALT_A, ALT_B, ALT_C, ALT_D,
                A, "Geografia"
        );

        // When
        HttpEntity<QuestaoMultiplaEscolha> requisicao = JwtHelper.criarRequisicaoComToken(questao, token);
        ResponseEntity<QuestaoBase> resposta = testRestTemplate.exchange(
                BASE_URL + "/post", HttpMethod.POST, requisicao, QuestaoBase.class);

        // Then
        assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
        assertNotNull(resposta.getBody());
    }


    @Test
    @DisplayName("✔ 02 - Deve listar todas as questões com sucesso")
    void deveListarTodasQuestoes() {

        // Given
        questaoRepository.save(
                TestBuilder.criarQuestao(null, "Questão 1 de História",
                        ALT_A, ALT_B, ALT_C, ALT_D, A,"Historia")
        );

        questaoRepository.save(
                TestBuilder.criarQuestao(null, "Questão 2 de Matemática",
                        ALT_A, ALT_B, ALT_C, ALT_D, A,"Matemática")
        );

        // When
        HttpEntity<Void> requisicao = JwtHelper.criarRequisicaoComToken(token);
        ResponseEntity<QuestaoBase[]> resposta = testRestTemplate.exchange(
                BASE_URL + "/all", HttpMethod.GET, requisicao, QuestaoBase[].class);

        // Then
        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        assertNotNull(resposta.getBody());
        // Ajustado para garantir que há pelo menos 2 questões após os saves
        // (considerando que outros testes também podem ter salvo)
        assertTrue(resposta.getBody().length >= 2);
    }


    @Test
    @DisplayName("✔ 03 - Deve buscar uma questão pelo ID com sucesso")
    void deveBuscarPorId() {

        // Given
        QuestaoBase salva = questaoRepository.save(
                TestBuilder.criarQuestao(null, "Questão teste ID para Biologia",
                        ALT_A, ALT_B, ALT_C, ALT_D, A,"Biologia")
        );

        // When
        HttpEntity<Void> requisicao = JwtHelper.criarRequisicaoComToken(token);
        ResponseEntity<QuestaoBase> resposta = testRestTemplate.exchange(
                BASE_URL + "/" + salva.getId(), HttpMethod.GET, requisicao, QuestaoBase.class);

        // Then
        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        assertNotNull(resposta.getBody());
        assertEquals(salva.getId(), resposta.getBody().getId());
    }


    @Test
    @DisplayName("✔ 04 - Deve atualizar uma questão com sucesso")
    void deveAtualizarQuestao() {

        // Given
        QuestaoBase salva = questaoRepository.save(
                TestBuilder.criarQuestao(null, "Questão antiga de Física",
                        ALT_A, ALT_B, ALT_C, ALT_D, A,"Física")
        );

        salva.setEnunciado("Questão de Física atualizada com novos detalhes");

        // When
        HttpEntity<QuestaoBase> requisicao = JwtHelper.criarRequisicaoComToken(salva, token);
        ResponseEntity<QuestaoBase> resposta = testRestTemplate.exchange(
                BASE_URL + "/put", HttpMethod.PUT, requisicao, QuestaoBase.class);

        // Then
        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        assertEquals("Questão de Física atualizada com novos detalhes", resposta.getBody().getEnunciado());
    }


    @Test
    @DisplayName("✔ 05 - Deve deletar uma questão com sucesso")
    void deveDeletarQuestao() {

        // Given
        QuestaoBase salva = questaoRepository.save(
                TestBuilder.criarQuestao(null, "Questão a deletar de Química",
                        ALT_A, ALT_B, ALT_C, ALT_D, A,"Química")
        );

        // When
        HttpEntity<Void> requisicao = JwtHelper.criarRequisicaoComToken(token);
        ResponseEntity<Void> resposta = testRestTemplate.exchange(
                BASE_URL + "/" + salva.getId(), HttpMethod.DELETE, requisicao, Void.class);

        // Then
        assertEquals(HttpStatus.NO_CONTENT, resposta.getStatusCode());
        assertEquals(Optional.empty(), questaoRepository.findById(salva.getId()));
    }

}