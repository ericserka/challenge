package com.challenge2.challenge2.excecoes;

import java.util.HashMap;

public record CorpoRequisicaoInvalidoDTO(String mensagem, Integer codigoStatus, HashMap<String, String> erros) {
}
