package com.challenge2.challenge2.dtos.cliente_pf.mapeadores;

import org.springframework.stereotype.Component;

import com.challenge2.challenge2.dominio.cliente_pf.ClientePF;
import com.challenge2.challenge2.dtos.cliente_pf.respostas.RespostaSelecaoClientePFDTO;

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
