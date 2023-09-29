package com.challenge4.challenge4.dtos.cliente_pf.respostas;

import java.time.Instant;
import java.util.UUID;

public record RespostaCriacaoClientePFDTO(
        UUID id,
        String cpf,
        String nome,
        Integer mcc,
        String email,
        Instant criadoEm) {
}
