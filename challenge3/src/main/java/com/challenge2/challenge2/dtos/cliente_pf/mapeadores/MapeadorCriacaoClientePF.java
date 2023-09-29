package com.challenge2.challenge2.dtos.cliente_pf.mapeadores;

import org.springframework.stereotype.Component;

import com.challenge2.challenge2.dominio.cliente_pf.ClientePF;
import com.challenge2.challenge2.dtos.cliente_pf.CriacaoClientePFDTO;
import com.challenge2.challenge2.dtos.cliente_pf.respostas.RespostaCriacaoClientePFDTO;
import com.challenge2.challenge2.utilitarios.UtilitariosGerais;

@Component
public class MapeadorCriacaoClientePF {
    public ClientePF paraEntidade(final CriacaoClientePFDTO criacaoClientePFDTO) {
        return new ClientePF(UtilitariosGerais.formatarCpf(criacaoClientePFDTO.cpf()), criacaoClientePFDTO.mcc(),
                criacaoClientePFDTO.nome(), criacaoClientePFDTO.email());
    }

    public RespostaCriacaoClientePFDTO paraDto(final ClientePF clientePF) {
        return new RespostaCriacaoClientePFDTO(
                clientePF.getId(),
                clientePF.getCpf(),
                clientePF.getNome(),
                clientePF.getMcc(),
                clientePF.getEmail(),
                clientePF.getCriadoEm());
    }
}
