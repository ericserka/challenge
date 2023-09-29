package com.challenge3.challenge3.excecoes;

import java.util.HashMap;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class InterceptorExcecao {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CorpoRequisicaoInvalidoDTO> trataExcecoesValidacao(
            final MethodArgumentNotValidException e) {
        final HashMap<String, String> erros = new HashMap<>();

        e.getBindingResult().getAllErrors().forEach(erro -> {
            String nomeCampo = ((FieldError) erro).getField();
            String mensagemErro = erro.getDefaultMessage();
            erros.put(nomeCampo, mensagemErro);
        });

        final CorpoRequisicaoInvalidoDTO corpoRequisicaoInvalidoDTO = new CorpoRequisicaoInvalidoDTO(
                "Corpo da requisição invalido",
                HttpStatus.BAD_REQUEST.value(), erros);

        log.error("Corpo da requisição invalido: {}", corpoRequisicaoInvalidoDTO);

        return ResponseEntity.badRequest().body(corpoRequisicaoInvalidoDTO);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ExcecaoDTO> trataExcecaoResponseStatus(final ResponseStatusException e) {
        final Integer codigoStatus = e.getStatusCode().value();
        final ExcecaoDTO excecaoDTO = new ExcecaoDTO(
                e.getReason(),
                codigoStatus);

        log.error("Excecao Response Status: {}", excecaoDTO);

        return ResponseEntity.status(codigoStatus).body(excecaoDTO);
    }

    @ExceptionHandler({ HttpMessageNotReadableException.class, MissingPathVariableException.class,
            IllegalArgumentException.class })
    public ResponseEntity<ExcecaoDTO> trataExcecoesRequisicao(final Exception e) {
        final Integer codigoStatus = HttpStatus.BAD_REQUEST.value();
        final ExcecaoDTO excecaoDTO = new ExcecaoDTO(e.getMessage(), codigoStatus);

        log.error("Excecao de requisicao: {}", excecaoDTO);

        return ResponseEntity.status(codigoStatus).body(excecaoDTO);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExcecaoDTO> trataExcecaoGenerica(final Exception e) {
        final Integer codigoStatus = HttpStatus.INTERNAL_SERVER_ERROR.value();
        final ExcecaoDTO excecaoDTO = new ExcecaoDTO(
                Optional.ofNullable(e.getMessage()).orElse("Erro interno do servidor"),
                codigoStatus);

        log.error("Erro interno: [{}]: {}", e.getClass().getSimpleName(), excecaoDTO);

        return ResponseEntity.status(codigoStatus).body(excecaoDTO);
    }
}
