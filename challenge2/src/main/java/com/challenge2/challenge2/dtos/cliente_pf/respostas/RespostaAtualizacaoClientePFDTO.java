package com.challenge2.challenge2.dtos.cliente_pf.respostas;

import java.time.Instant;
import java.util.UUID;

public record RespostaAtualizacaoClientePFDTO(
                UUID id,
                String cpf,
                String nome,
                Integer mcc,
                String email,
                Instant criadoEm,
                Instant atualizadoEm) {
}
