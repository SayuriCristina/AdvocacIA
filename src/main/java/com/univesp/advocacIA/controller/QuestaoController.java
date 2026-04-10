package com.univesp.advocacIA.controller;

import com.univesp.advocacIA.model.QuestaoBase;
import com.univesp.advocacIA.model.QuestaoMultiplaEscolha;
import com.univesp.advocacIA.model.QuestaoDissertativa;
import com.univesp.advocacIA.repository.QuestaoRepository;

import io.micrometer.common.lang.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/questao")
@CrossOrigin(origins = "*", allowedHeaders = "*") // TODO: Colocar a origem definitiva

public class QuestaoController {

    @Autowired
    private QuestaoRepository questaoRepository;

    private void validarQuestao(QuestaoBase questao) {
        if (questao.getEnunciado() == null || questao.getEnunciado().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O enunciado não pode estar vazio");
        }
        if (questao.getEnunciado().length() < 5) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O enunciado deve ter pelo menos 5 caracteres");
        }

        if (questao instanceof QuestaoMultiplaEscolha) {
            validarQuestaoMultiplaEscolha((QuestaoMultiplaEscolha) questao);
        } else if (questao instanceof QuestaoDissertativa) {
            validarQuestaoDissertativa((QuestaoDissertativa) questao);
        }
    }

    private void validarQuestaoMultiplaEscolha(QuestaoMultiplaEscolha questao) {
        if (questao.getAlternativaA() == null || questao.getAlternativaA().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Alternativa A não pode estar vazia");
        }
        if (questao.getAlternativaA().length() < 5) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Alternativa A deve ter pelo menos 5 caracteres");
        }
        if (questao.getAlternativaB() == null || questao.getAlternativaB().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Alternativa B não pode estar vazia");
        }
        if (questao.getAlternativaB().length() < 5) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Alternativa B deve ter pelo menos 5 caracteres");
        }
        if (questao.getAlternativaC() == null || questao.getAlternativaC().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Alternativa C não pode estar vazia");
        }
        if (questao.getAlternativaC().length() < 5) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Alternativa C deve ter pelo menos 5 caracteres");
        }
        if (questao.getAlternativaD() == null || questao.getAlternativaD().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Alternativa D não pode estar vazia");
        }
        if (questao.getAlternativaD().length() < 5) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Alternativa D deve ter pelo menos 5 caracteres");
        }
        if (questao.getResposta() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A resposta correta deve ser informada");
        }
    }

    private void validarQuestaoDissertativa(QuestaoDissertativa questao) {
        if (questao.getRespostaEsperada() == null || questao.getRespostaEsperada().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A resposta esperada não pode estar vazia");
        }
        if (questao.getRespostaEsperada().length() < 10) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A resposta esperada deve ter pelo menos 10 caracteres");
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<QuestaoBase>> getAll(){
        return ResponseEntity.ok(questaoRepository.findAll()); // Pega todas as questões, usado apenas para fins de teste
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestaoBase> getById(@PathVariable @NonNull Long id){
        return questaoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/assunto/{assunto}")
    public ResponseEntity<List<QuestaoBase>> getByAssunto(@PathVariable String assunto){
        return ResponseEntity.ok(questaoRepository.findAllByAssuntoContainingIgnoreCase(assunto));
    }

    @PostMapping ("/post")
    public ResponseEntity<QuestaoBase> post(@RequestBody QuestaoBase questao) {
        validarQuestao(questao);
        questao.setId(null);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(questaoRepository.save(questao)); // Cria uma questão, usado apenas por administradores

        // TODO: Proteger o endpoint
    }

    @PostMapping ("/post/batch")
    public ResponseEntity<List<QuestaoBase>> postBatch(@RequestBody List<QuestaoBase> questoes) {
        questoes.forEach(this::validarQuestao);
        questoes.forEach(questao -> questao.setId(null));
        List<QuestaoBase> savedQuestoes = questaoRepository.saveAll(questoes);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedQuestoes); // Cria uma lista de questões, usado apenas por administradores

        // TODO: Proteger o endpoint
    }

    @PutMapping("/put")
    public ResponseEntity<QuestaoBase> put(@RequestBody QuestaoBase questao) {
        validarQuestao(questao);
        return questaoRepository.findById(questao.getId())
                .map(resposta -> ResponseEntity.status(HttpStatus.OK)
                        .body(questaoRepository.save(questao)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable @NonNull Long id) {
        Optional<QuestaoBase> questao = questaoRepository.findById(id);

        if(questao.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        questaoRepository.deleteById(id);
    }

}
