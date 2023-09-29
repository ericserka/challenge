package com.challenge3.challenge3.excecoes;

import java.util.HashMap;

public record CorpoRequisicaoInvalidoDTO(String mensagem, Integer codigoStatus, HashMap<String, String> erros) {
}
