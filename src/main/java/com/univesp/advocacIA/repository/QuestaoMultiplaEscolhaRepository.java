package com.univesp.advocacIA.repository;

import com.univesp.advocacIA.model.QuestaoMultiplaEscolha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestaoMultiplaEscolhaRepository extends JpaRepository<QuestaoMultiplaEscolha, Long> {

    public List<QuestaoMultiplaEscolha> findAllByAssuntoContainingIgnoreCase(@Param("assunto") String assunto);

}
