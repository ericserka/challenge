package com.challenge2.challenge2.excecoes;

public class FilaVaziaExcecao extends RuntimeException {
    public FilaVaziaExcecao() {
        super("Fila está vazia");
    }
}
