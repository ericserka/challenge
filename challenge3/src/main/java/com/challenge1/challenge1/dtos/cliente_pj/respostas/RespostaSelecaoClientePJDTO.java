package com.challenge1.challenge1.dtos.cliente_pj.respostas;

import java.time.Instant;
import java.util.UUID;

public record RespostaSelecaoClientePJDTO(
        UUID id,
        String cnpj,
        String razaoSocial,
        Integer mcc,
        String cpfContato,
        String nomeContato,
        String emailContato,
        Instant criadoEm,
        Instant atualizadoEm) {
}
