package com.challenge4.challenge4.servicos;

import org.springframework.stereotype.Service;

import com.challenge4.challenge4.dominio.cliente.Cliente;
import com.challenge4.challenge4.dominio.cliente_pf.ClientePF;
import com.challenge4.challenge4.dominio.cliente_pj.ClientePJ;
import com.challenge4.challenge4.infra.aws.ClienteMemoryDB;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ServicoRedis {
    private final ClienteMemoryDB clienteMemoryDB;

    public void salvarClientePF(final ClientePF clientePF) {
        final Cliente cliente = new Cliente(clientePF);
        salvar(cliente.getId().toString(), cliente);
    }

    public void salvarClientePJ(final ClientePJ clientePJ) {
        final Cliente cliente = new Cliente(clientePJ);
        salvar(cliente.getId().toString(), cliente);
    }

    private void salvar(final String chave, final Cliente cliente) {
        clienteMemoryDB.upsertCliente(chave, cliente);
    }

    public Cliente buscar(final String chave) {
        return clienteMemoryDB.getCliente(chave);
    }

    public void deletar(final String chave) {
        clienteMemoryDB.deleteCliente(chave);
    }

}