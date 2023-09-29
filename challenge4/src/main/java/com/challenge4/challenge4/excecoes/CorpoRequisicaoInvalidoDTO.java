package com.challenge4.challenge4.excecoes;

import java.util.HashMap;

public record CorpoRequisicaoInvalidoDTO(String mensagem, Integer codigoStatus, HashMap<String, String> erros) {
}
