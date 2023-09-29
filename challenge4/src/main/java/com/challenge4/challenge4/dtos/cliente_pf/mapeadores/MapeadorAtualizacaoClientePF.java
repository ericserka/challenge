package com.challenge4.challenge4.dtos.cliente_pf.mapeadores;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.challenge4.challenge4.dominio.cliente_pf.ClientePF;
import com.challenge4.challenge4.dtos.cliente_pf.AtualizacaoClientePFDTO;
import com.challenge4.challenge4.dtos.cliente_pf.respostas.RespostaAtualizacaoClientePFDTO;
import com.challenge4.challenge4.utilitarios.UtilitariosGerais;

@Component
public class MapeadorAtualizacaoClientePF {
    public RespostaAtualizacaoClientePFDTO paraDto(ClientePF clientePF) {
        return new RespostaAtualizacaoClientePFDTO(
                clientePF.getId(),
                clientePF.getCpf(),
                clientePF.getNome(),
                clientePF.getMcc(),
                clientePF.getEmail(),
                clientePF.getCriadoEm(),
                clientePF.getAtualizadoEm());
    }

    public ClientePF paraEntidade(final UUID id,
            final AtualizacaoClientePFDTO atualizacaoClientePFDTO) {
        ClientePF clientePF = new ClientePF(UtilitariosGerais.formatarCpf(atualizacaoClientePFDTO.cpf()),
                atualizacaoClientePFDTO.mcc(), atualizacaoClientePFDTO.nome(), atualizacaoClientePFDTO.email());

        clientePF.setId(id);

        return clientePF;
    }
}
