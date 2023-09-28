package com.challenge2.challenge2.utilitarios;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Test;

import com.challenge2.challenge2.utilitarios.UtilitariosGerais;

class UtilitariosGeraisTest {

    @Test
    void formatarCnpj_quandoDigitosMenor14_entaoAdicionaZeros() {
        final String cnpjFormatado = UtilitariosGerais.formatarCnpj(1234567890L);

        assertThat(cnpjFormatado.length(), equalTo(14));
        assertThat(cnpjFormatado, equalTo("00001234567890"));
    }

    @Test
    void formatarCpf_quandoDigitosMenor11_entaoAdicionaZeros() {
        final String cpfFormatado = UtilitariosGerais.formatarCpf(1234567890L);

        assertThat(cpfFormatado.length(), equalTo(11));
        assertThat(cpfFormatado, equalTo("01234567890"));
    }

    @Test
    void formatarCnpj_quandoDigitosIgual14_entaoNaoAdicionaZeros() {
        final String cnpjFormatado = UtilitariosGerais.formatarCnpj(12345678901234L);

        assertThat(cnpjFormatado.length(), equalTo(14));
        assertThat(cnpjFormatado, equalTo("12345678901234"));
    }

    @Test
    void formatarCpf_quandoDigitosIgual11_entaoNaoAdicionaZeros() {
        final String cpfFormatado = UtilitariosGerais.formatarCpf(12345678901L);

        assertThat(cpfFormatado.length(), equalTo(11));
        assertThat(cpfFormatado, equalTo("12345678901"));
    }

    @Test
    void removerFormatacaoCpfCnpj_removeZeros_converteParaLong() {
        final String cpf = "01234567890";
        final String cnpj = "01234567890123";

        final Long longCpf = UtilitariosGerais.removerFormatacaoCpfCnpj(cpf);
        final Long longCnpj = UtilitariosGerais.removerFormatacaoCpfCnpj(cnpj);

        assertThat(longCpf, equalTo(1234567890L));
        assertThat(longCnpj, equalTo(1234567890123L));
    }
}
