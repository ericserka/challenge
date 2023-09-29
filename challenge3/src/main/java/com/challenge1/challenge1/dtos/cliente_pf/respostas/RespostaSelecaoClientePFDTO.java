package com.challenge1.challenge1.dtos.cliente_pf.respostas;

import java.time.Instant;
import java.util.UUID;

public record RespostaSelecaoClientePFDTO(
                UUID id,
                String cpf,
                String nome,
                Integer mcc,
                String email,
                Instant criadoEm,
                Instant atualizadoEm) {
}
