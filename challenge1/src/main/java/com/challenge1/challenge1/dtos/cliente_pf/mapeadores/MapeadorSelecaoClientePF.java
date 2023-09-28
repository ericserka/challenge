package com.challenge1.challenge1.dtos.cliente_pf.mapeadores;

import org.springframework.stereotype.Component;

import com.challenge1.challenge1.dominio.cliente_pf.ClientePF;
import com.challenge1.challenge1.dtos.cliente_pf.respostas.RespostaSelecaoClientePFDTO;

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
