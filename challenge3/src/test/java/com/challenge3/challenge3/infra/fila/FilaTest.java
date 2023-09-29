package com.challenge3.challenge3.infra.fila;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.challenge3.challenge3.excecoes.FilaVaziaExcecao;

class FilaTest {
    @Test
    void criarFilaVazia() {
        final Fila<String> fila = new Fila<>();

        assertThat(fila.estaVazia(), is(true));
    }

    @Test
    void adicionarUmElemento() {
        final Fila<String> fila = new Fila<>();

        fila.push("primeiro");
        fila.push("segundo");

        assertThat(fila.tamanho(), is(2L));
        assertThat(fila.estaVazia(), is(false));
        assertThat(fila.inicio(), is("primeiro"));
    }

    @Test
    void removerUmElemento() {
        final Fila<String> fila = new Fila<>();

        fila.push("primeiro");
        fila.push("segundo");
        fila.push("terceiro");

        assertThat(fila.pop(), is("primeiro"));
        assertThat(fila.tamanho(), is(2L));
        assertThat(fila.inicio(), is("segundo"));
        assertThat(fila.estaVazia(), is(false));

        assertThat(fila.pop(), is("segundo"));
        assertThat(fila.tamanho(), is(1L));
        assertThat(fila.inicio(), is("terceiro"));
        assertThat(fila.estaVazia(), is(false));

        assertThat(fila.pop(), is("terceiro"));
        assertThat(fila.tamanho(), is(0L));
        assertThat(fila.estaVazia(), is(true));
    }

    @Test
    void limparFila() {
        final Fila<String> fila = new Fila<>();

        fila.push("primeiro");
        fila.push("segundo");
        fila.push("terceiro");

        fila.limpar();

        assertThat(fila.tamanho(), is(0L));
        assertThat(fila.estaVazia(), is(true));
    }

    @Test
    void adicionarUmElementoNaFilaVazia() {
        final Fila<String> fila = new Fila<>();

        fila.push("primeiro");

        assertThat(fila.tamanho(), is(1L));
        assertThat(fila.estaVazia(), is(false));
        assertThat(fila.inicio(), is("primeiro"));
    }

    @Test
    void lancarExcecaoAoRemoverElementoDaFilaVazia() {
        final Fila<String> fila = new Fila<>();
        final Throwable thrown = assertThrows(FilaVaziaExcecao.class, () -> fila.pop());

        assertThat(thrown.getMessage(), is("Fila está vazia"));
    }

    @Test
    void lancarExcecaoAoConsultarElementoDaFilaVazia() {
        final Fila<String> fila = new Fila<>();
        final Throwable thrown = assertThrows(FilaVaziaExcecao.class, () -> fila.inicio());

        assertThat(thrown.getMessage(), is("Fila está vazia"));
    }

    @Test
    void obterPrimeiroElementoFila() {
        final Fila<String> fila = new Fila<>();
        fila.push("primeiro");
        fila.push("segundo");

        assertThat(fila.inicio(), is("primeiro"));
    }

    @Test
    void obterUltimoElementoFila() {
        final Fila<String> fila = new Fila<>();
        fila.push("primeiro");
        fila.push("segundo");

        assertThat(fila.fim(), is("segundo"));
    }
}