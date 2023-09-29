package com.challenge3.challenge3.infra.fila;

import org.springframework.stereotype.Component;

import com.challenge3.challenge3.excecoes.FilaVaziaExcecao;

@Component
public class Fila<T> {
    private Nodulo<T> inicio;
    private Nodulo<T> fim;

    public Fila() {
        this.inicio = null;
        this.fim = null;
    }

    public boolean estaVazia() {
        return this.inicio == null && this.fim == null;
    }

    public void push(final T elemento) {
        final Nodulo<T> novoNodulo = new Nodulo<>(elemento);
        if (estaVazia()) {
            this.inicio = novoNodulo;
            this.fim = novoNodulo;
        } else {
            this.fim.setProximo(novoNodulo);
            this.fim = novoNodulo;
        }
    }

    public T pop() {
        if (estaVazia()) {
            throw new FilaVaziaExcecao();
        }

        T elemento = this.inicio.getValor();
        this.inicio = this.inicio.getProximo();
        if (this.inicio == null) {
            this.fim = null;
        }

        return elemento;
    }

    public T inicio() {
        if (estaVazia()) {
            throw new FilaVaziaExcecao();
        }
        return this.inicio.getValor();
    }

    public T fim() {
        if (estaVazia()) {
            throw new FilaVaziaExcecao();
        }
        return this.fim.getValor();
    }

    public void limpar() {
        while (!estaVazia()) {
            pop();
        }
    }

    public Long tamanho() {
        Long tamanho = 0L;
        for (Nodulo<T> nodulo = this.inicio; nodulo != null; nodulo = nodulo.getProximo()) {
            tamanho++;
        }
        return tamanho;
    }
}