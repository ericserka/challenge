package com.challenge2.challenge2.dtos.cliente_pj.mapeadores;

import org.springframework.stereotype.Component;

import com.challenge2.challenge2.dominio.cliente_pj.ClientePJ;
import com.challenge2.challenge2.dtos.cliente_pj.CriacaoClientePJDTO;
import com.challenge2.challenge2.dtos.cliente_pj.respostas.RespostaCriacaoClientePJDTO;
import com.challenge2.challenge2.utilitarios.UtilitariosGerais;

@Component
public class MapeadorCriacaoClientePJ {
    public ClientePJ paraEntidade(final CriacaoClientePJDTO criacaoClientePJDTO) {
        return new ClientePJ(
                UtilitariosGerais.formatarCnpj(criacaoClientePJDTO.cnpj()),
                criacaoClientePJDTO.razaoSocial(),
                criacaoClientePJDTO.mcc(),
                UtilitariosGerais.formatarCpf(criacaoClientePJDTO.cpfContato()),
                criacaoClientePJDTO.nomeContato(),
                criacaoClientePJDTO.emailContato());
    }

    public RespostaCriacaoClientePJDTO paraDto(final ClientePJ clientePJ) {
        return new RespostaCriacaoClientePJDTO(
                clientePJ.getId(),
                clientePJ.getCnpj(),
                clientePJ.getRazaoSocial(),
                clientePJ.getMcc(),
                clientePJ.getCpfContato(),
                clientePJ.getNomeContato(),
                clientePJ.getEmailContato(),
                clientePJ.getCriadoEm());
    }
}
