package com.challenge4.challenge4.dtos.cliente_pj.mapeadores;

import org.springframework.stereotype.Component;

import com.challenge4.challenge4.dominio.cliente_pj.ClientePJ;
import com.challenge4.challenge4.dtos.cliente_pj.respostas.RespostaSelecaoClientePJDTO;

@Component
public class MapeadorSelecaoClientePJ {
    public RespostaSelecaoClientePJDTO paraDto(ClientePJ clientePJ) {
        return new RespostaSelecaoClientePJDTO(
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
}
