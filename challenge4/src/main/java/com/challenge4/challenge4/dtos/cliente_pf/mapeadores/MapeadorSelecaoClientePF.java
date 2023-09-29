package com.challenge4.challenge4.dtos.cliente_pf.mapeadores;

import org.springframework.stereotype.Component;

import com.challenge4.challenge4.dominio.cliente_pf.ClientePF;
import com.challenge4.challenge4.dtos.cliente_pf.respostas.RespostaSelecaoClientePFDTO;

@Component
public class MapeadorSelecaoClientePF {
    public RespostaSelecaoClientePFDTO paraDto(ClientePF clientePF) {
        return new RespostaSelecaoClientePFDTO(
                clientePF.getId(),
                clientePF.getCpf(),
                clientePF.getNome(),
                clientePF.getMcc(),
                clientePF.getEmail(),
                clientePF.getCriadoEm(),
                clientePF.getAtualizadoEm());
    }
}
