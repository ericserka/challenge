package com.challenge4.challenge4.controladoras.fila;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.challenge4.challenge4.dominio.cliente.Cliente;
import com.challenge4.challenge4.excecoes.FilaVaziaExcecao;
import com.challenge4.challenge4.servicos.ServicoFila;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping(value = "/fila", produces = "application/json")
@AllArgsConstructor
public class ControladoraFilaImpl implements ControladoraFila {
    private final ServicoFila servico;

    public ResponseEntity<Cliente> desenfileirarCliente() {
        try {
            final Cliente resposta = this.servico.desenfileirarCliente();
            return new ResponseEntity<>(resposta, HttpStatus.OK);
        } catch (FilaVaziaExcecao e) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, e.getMessage());
        }
    }
}
