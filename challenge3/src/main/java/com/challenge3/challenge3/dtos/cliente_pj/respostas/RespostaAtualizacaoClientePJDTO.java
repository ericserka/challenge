package com.challenge3.challenge3.dtos.cliente_pj.respostas;

import java.time.Instant;
import java.util.UUID;

public record RespostaAtualizacaoClientePJDTO(
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
