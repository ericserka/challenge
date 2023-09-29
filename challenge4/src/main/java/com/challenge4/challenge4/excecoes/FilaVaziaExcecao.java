package com.challenge4.challenge4.excecoes;

public class FilaVaziaExcecao extends RuntimeException {
    public FilaVaziaExcecao() {
        super("Fila está vazia");
    }
}
