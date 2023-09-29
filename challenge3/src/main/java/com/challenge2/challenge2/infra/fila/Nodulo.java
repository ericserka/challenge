package com.challenge2.challenge2.infra.fila;

public class Nodulo<T> {
    private T valor;
    private Nodulo<T> proximo;

    public Nodulo(T valor) {
        this.valor = valor;
        this.proximo = null;
    }

    public T getValor() {
        return valor;
    }

    public Nodulo<T> getProximo() {
        return proximo;
    }

    public void setProximo(Nodulo<T> proximo) {
        this.proximo = proximo;
    }
}