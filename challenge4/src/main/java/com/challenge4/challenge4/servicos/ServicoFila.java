package com.challenge4.challenge4.servicos;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.challenge4.challenge4.dominio.cliente.Cliente;
import com.challenge4.challenge4.dominio.cliente_pf.ClientePF;
import com.challenge4.challenge4.dominio.cliente_pj.ClientePJ;
import com.challenge4.challenge4.infra.aws.ClienteSqs;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.sqs.model.Message;

@Service
@AllArgsConstructor
@Slf4j
public class ServicoFila {
    private final ClienteSqs clienteSqs;
    private final ObjectMapper objectMapper;

    public Cliente desenfileirarCliente() {
        final Cliente cliente = this.readMessage();
        log.info("Retirando cliente de id {} da fila", cliente.getId());
        return cliente;
    }

    public void enfileirarClientePF(ClientePF clientePF) {
        log.info("Enfileirando cliente PF de id {} na fila", clientePF.getId());
        final Cliente cliente = new Cliente(clientePF);
        this.clienteSqs.sendMessage(cliente);
    }

    public void enfileirarClientePJ(ClientePJ clientePJ) {
        log.info("Enfileirando cliente PJ de id {} na fila", clientePJ.getId());
        final Cliente cliente = new Cliente(clientePJ);
        this.clienteSqs.sendMessage(cliente);
    }

    private Cliente readMessage() {
        final Message message = this.clienteSqs.receiveMessage();
        final String body = message.body();
        this.clienteSqs.deleteMessage(message);

        try {
            return this.objectMapper.readValue(body, Cliente.class);
        } catch (JsonProcessingException e) {
            log.error("Erro ao deserializar mensagem da fila: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "falha ao deserializar mensagem da fila");
        }
    }
}
