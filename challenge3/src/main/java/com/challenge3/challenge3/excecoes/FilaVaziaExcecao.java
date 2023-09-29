package com.challenge3.challenge3.excecoes;

public class FilaVaziaExcecao extends RuntimeException {
    public FilaVaziaExcecao() {
        super("Fila está vazia");
    }
}
