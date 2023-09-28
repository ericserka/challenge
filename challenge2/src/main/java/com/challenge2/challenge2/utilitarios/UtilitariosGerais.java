package com.challenge2.challenge2.utilitarios;

import org.springframework.stereotype.Component;

@Component
public class UtilitariosGerais {
    private UtilitariosGerais() {
    }

    public static String formatarCpf(Long cpf) {
        return String.format("%011d", cpf);
    }

    public static String formatarCnpj(Long cnpj) {
        return String.format("%014d", cnpj);
    }

    public static Long removerFormatacaoCpfCnpj(String cpfCnpj) {
        return Long.parseLong(cpfCnpj.replaceFirst("^0+", ""));
    }
}
