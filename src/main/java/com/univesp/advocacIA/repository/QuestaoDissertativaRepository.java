package com.univesp.advocacIA.repository;

import com.univesp.advocacIA.model.QuestaoDissertativa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestaoDissertativaRepository extends JpaRepository<QuestaoDissertativa, Long> {

    public List<QuestaoDissertativa> findAllByAssuntoContainingIgnoreCase(@Param("assunto") String assunto);

}
