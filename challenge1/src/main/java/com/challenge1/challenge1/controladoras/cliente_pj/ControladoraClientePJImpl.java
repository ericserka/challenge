package com.challenge1.challenge1.controladoras.cliente_pj;

import java.util.List;
import java.util.UUID;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.challenge1.challenge1.dtos.cliente_pj.AtualizacaoClientePJDTO;
import com.challenge1.challenge1.dtos.cliente_pj.CriacaoClientePJDTO;
import com.challenge1.challenge1.dtos.cliente_pj.respostas.RespostaAtualizacaoClientePJDTO;
import com.challenge1.challenge1.dtos.cliente_pj.respostas.RespostaCriacaoClientePJDTO;
import com.challenge1.challenge1.dtos.cliente_pj.respostas.RespostaSelecaoClientePJDTO;
import com.challenge1.challenge1.servicos.ServicoClientePJ;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Validated
@RestController
@RequestMapping(value = "/clientes-pj", produces = "application/json")
@AllArgsConstructor
public class ControladoraClientePJImpl implements ControladoraClientePJ {
    private static final String UUID_INVALIDO_MENSAGEM_ERRO = "UUID invalido: %s";
    private static final String CONFLITO_MENSAGEM_ERRO = "Ja existe um cliente PJ para os dados fornecidos";

    private final ServicoClientePJ servico;

    public ResponseEntity<List<RespostaSelecaoClientePJDTO>> listarTodos() {
        final List<RespostaSelecaoClientePJDTO> resposta = this.servico.listarTodos();
        return new ResponseEntity<>(resposta, HttpStatus.OK);
    }

    public ResponseEntity<RespostaSelecaoClientePJDTO> acharPorId(
            @PathVariable final String id) {
        try {
            final RespostaSelecaoClientePJDTO resposta = this.servico.acharPorId(UUID.fromString(id));
            return new ResponseEntity<>(resposta, HttpStatus.OK);
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format(UUID_INVALIDO_MENSAGEM_ERRO, id));
        }
    }

    public void remover(@PathVariable final String id) {
        try {
            this.servico.remover(UUID.fromString(id));
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format(UUID_INVALIDO_MENSAGEM_ERRO, id));
        }
    }

    public ResponseEntity<RespostaAtualizacaoClientePJDTO> atualizar(
            @PathVariable final String id,
            @RequestBody @Valid final AtualizacaoClientePJDTO atualizacaoClientePJDTO) {
        try {
            final RespostaAtualizacaoClientePJDTO resposta = this.servico.atualizar(UUID.fromString(id),
                    atualizacaoClientePJDTO);
            return new ResponseEntity<>(resposta, HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, CONFLITO_MENSAGEM_ERRO);
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format(UUID_INVALIDO_MENSAGEM_ERRO, id));
        }
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<RespostaCriacaoClientePJDTO> criar(
            @RequestBody @Valid final CriacaoClientePJDTO criacaoClientePJDTO) {
        try {
            final RespostaCriacaoClientePJDTO resposta = this.servico.criar(criacaoClientePJDTO);
            return new ResponseEntity<>(resposta, HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, CONFLITO_MENSAGEM_ERRO);
        }
    }
}