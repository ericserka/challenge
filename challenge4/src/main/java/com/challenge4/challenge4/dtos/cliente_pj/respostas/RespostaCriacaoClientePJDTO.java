package com.challenge4.challenge4.dtos.cliente_pj.respostas;

import java.time.Instant;
import java.util.UUID;

public record RespostaCriacaoClientePJDTO(
                UUID id,
                String cnpj,
                String razaoSocial,
                Integer mcc,
                String cpfContato,
                String nomeContato,
                String emailContato,
                Instant criadoEm) {
}
