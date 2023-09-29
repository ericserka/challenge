package com.challenge3.challenge3.servicos;

import org.springframework.stereotype.Service;

import com.challenge3.challenge3.dominio.cliente.Cliente;
import com.challenge3.challenge3.dominio.cliente_pf.ClientePF;
import com.challenge3.challenge3.dominio.cliente_pj.ClientePJ;
import com.challenge3.challenge3.infra.fila.Fila;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class ServicoFila {
    private final Fila<Cliente> fila;

    public Cliente desenfileirarCliente() {
        Cliente cliente = this.fila.pop();
        log.info("Retirando cliente de id {} da fila", cliente.getId());
        return cliente;
    }

    public void enfileirarClientePF(ClientePF clientePF) {
        log.info("Enfileirando cliente PF de id {} na fila", clientePF.getId());
        final Cliente cliente = new Cliente(clientePF);
        this.fila.push(cliente);
    }

    public void enfileirarClientePJ(ClientePJ clientePJ) {
        log.info("Enfileirando cliente PJ de id {} na fila", clientePJ.getId());
        final Cliente cliente = new Cliente(clientePJ);
        this.fila.push(cliente);
    }

}
