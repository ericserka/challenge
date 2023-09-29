package com.challenge2.challenge2.dtos.cliente_pj.respostas;

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
