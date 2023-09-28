package com.challenge1.challenge1.excecoes;

import java.util.HashMap;

public record CorpoRequisicaoInvalidoDTO(String mensagem, Integer codigoStatus, HashMap<String, String> erros) {
}
