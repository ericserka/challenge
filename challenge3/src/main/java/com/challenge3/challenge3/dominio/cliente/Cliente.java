package com.challenge3.challenge3.dominio.cliente;

import java.time.Instant;
import java.util.UUID;

import com.challenge3.challenge3.dominio.cliente_pf.ClientePF;
import com.challenge3.challenge3.dominio.cliente_pj.ClientePJ;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class Cliente {
    private final UUID id;

    private final Instant criadoEm;

    private final Instant atualizadoEm;

    private final Integer mcc;

    private final TipoCliente tipoCliente;

    private String cpf;

    private String nome;

    private String email;

    private String cnpj;

    private String razaoSocial;

    private String cpfContato;

    private String nomeContato;

    private String emailContato;

    public Cliente(final ClientePJ clientePJ) {
        this(clientePJ.getId(), clientePJ.getCriadoEm(), clientePJ.getAtualizadoEm(), clientePJ.getMcc(),
                TipoCliente.PJ);
        this.cnpj = clientePJ.getCnpj();
        this.razaoSocial = clientePJ.getRazaoSocial();
        this.cpfContato = clientePJ.getCpfContato();
        this.nomeContato = clientePJ.getNomeContato();
        this.emailContato = clientePJ.getEmailContato();
    }

    public Cliente(final ClientePF clientePF) {
        this(clientePF.getId(), clientePF.getCriadoEm(), clientePF.getAtualizadoEm(), clientePF.getMcc(),
                TipoCliente.PF);
        this.cpf = clientePF.getCpf();
        this.nome = clientePF.getNome();
        this.email = clientePF.getEmail();
    }
}
