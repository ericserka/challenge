package com.challenge2.challenge2.dtos.cliente_pj.mapeadores;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.challenge2.challenge2.dominio.cliente_pj.ClientePJ;
import com.challenge2.challenge2.dtos.cliente_pj.AtualizacaoClientePJDTO;
import com.challenge2.challenge2.dtos.cliente_pj.respostas.RespostaAtualizacaoClientePJDTO;
import com.challenge2.challenge2.utilitarios.UtilitariosGerais;

@Component
public class MapeadorAtualizacaoClientePJ {
    public RespostaAtualizacaoClientePJDTO paraDto(ClientePJ clientePJ) {
        return new RespostaAtualizacaoClientePJDTO(
                clientePJ.getId(),
                clientePJ.getCnpj(),
                clientePJ.getRazaoSocial(),
                clientePJ.getMcc(),
                clientePJ.getCpfContato(),
                clientePJ.getNomeContato(),
                clientePJ.getEmailContato(),
                clientePJ.getCriadoEm(),
                clientePJ.getAtualizadoEm());
    }

    public ClientePJ paraEntidade(final UUID id,
            final AtualizacaoClientePJDTO atualizacaoClientePJDTO) {
        ClientePJ clientePJ = new ClientePJ(UtilitariosGerais.formatarCnpj(atualizacaoClientePJDTO.cnpj()),
                atualizacaoClientePJDTO.razaoSocial(), atualizacaoClientePJDTO.mcc(),
                UtilitariosGerais.formatarCpf(atualizacaoClientePJDTO.cpfContato()),
                atualizacaoClientePJDTO.nomeContato(), atualizacaoClientePJDTO.emailContato());

        clientePJ.setId(id);

        return clientePJ;
    }
}
