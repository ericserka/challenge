package com.challenge2.challenge2.controladoras.cliente_pf;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.challenge2.challenge2.dominio.cliente_pf.ClientePF;
import com.challenge2.challenge2.dtos.cliente_pf.AtualizacaoClientePFDTO;
import com.challenge2.challenge2.dtos.cliente_pf.CriacaoClientePFDTO;
import com.challenge2.challenge2.dtos.cliente_pf.respostas.RespostaAtualizacaoClientePFDTO;
import com.challenge2.challenge2.dtos.cliente_pf.respostas.RespostaCriacaoClientePFDTO;
import com.challenge2.challenge2.dtos.cliente_pf.respostas.RespostaSelecaoClientePFDTO;
import com.challenge2.challenge2.repositorios.RepositorioClientePF;
import com.challenge2.challenge2.utilitarios.UtilitariosGerais;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;

@SpringBootTest
@AutoConfigureMockMvc
public class ControladoraClientePFTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RepositorioClientePF repositorioClientePF;

    private Faker faker = new Faker();

    @AfterEach
    void tearDown() {
        this.deleteClientes();
    }

    @Test
    void listarTodos() throws Exception {
        this.saveCliente();
        this.saveCliente();

        final MvcResult resposta = mockMvc.perform(get("/clientes-pf"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[*].id").isNotEmpty())
                .andReturn();

        final String responseBody = resposta.getResponse().getContentAsString();
        final List<ClientePF> clientesPF = objectMapper.readValue(responseBody, List.class);

        assertThat(clientesPF, hasSize((int) this.repositorioClientePF.count()));
    }

    @Test
    void acharPorIdExistente() throws Exception {
        final UUID id = this.saveCliente().getId();

        final MvcResult resposta = mockMvc.perform(get("/clientes-pf/" + id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andReturn();

        final String responseBody = resposta.getResponse().getContentAsString();
        final RespostaSelecaoClientePFDTO clientePF = objectMapper.readValue(responseBody,
                RespostaSelecaoClientePFDTO.class);

        assertThat(clientePF.id(), equalTo(id));
        assertThat(this.repositorioClientePF.findAll(), hasSize(1));
    }

    @Test
    void removerPorIdExistente() throws Exception {
        final UUID id = this.saveCliente().getId();

        mockMvc.perform(delete("/clientes-pf/" + id))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertThat(this.repositorioClientePF.findAll(), hasSize(0));
    }

    @Test
    void atualizarSucesso() throws Exception {
        final ClientePF clientePF = this.saveCliente();
        final UUID id = clientePF.getId();

        final AtualizacaoClientePFDTO putBody = new AtualizacaoClientePFDTO(1234,
                UtilitariosGerais.removerFormatacaoCpfCnpj(faker.numerify("###########")), "nome", "email@email.com");

        final MvcResult resposta = mockMvc
                .perform(put("/clientes-pf/" + id).content(objectMapper.writeValueAsString(putBody))
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andReturn();

        final String responseBody = resposta.getResponse().getContentAsString();
        final RespostaAtualizacaoClientePFDTO clientePFAtualizado = objectMapper.readValue(responseBody,
                RespostaAtualizacaoClientePFDTO.class);

        assertThat(this.repositorioClientePF.findAll(), hasSize(1));
        assertThat(clientePFAtualizado.id(), equalTo(id));
        assertThat(UtilitariosGerais.removerFormatacaoCpfCnpj(clientePFAtualizado.cpf()), equalTo(putBody.cpf()));
        assertThat(clientePFAtualizado.nome(), equalTo(putBody.nome()));
    }

    @Test
    void atualizarErroUniqueCpfEmail() throws Exception {
        final ClientePF cliente1 = this.saveCliente();
        final ClientePF cliente2 = this.saveCliente();

        final UUID id = cliente2.getId();
        final Long cpf = UtilitariosGerais.removerFormatacaoCpfCnpj(cliente1.getCpf());
        final String email = cliente1.getEmail();

        final AtualizacaoClientePFDTO putBody = new AtualizacaoClientePFDTO(1234, cpf, "nome", email);

        mockMvc.perform(put("/clientes-pf/" + id).content(objectMapper.writeValueAsString(putBody))
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").doesNotExist())
                .andExpect(content().string(containsString("Ja existe um cliente PF para os dados fornecidos")));
    }

    @Test
    void criar() throws Exception {
        final CriacaoClientePFDTO postBody = new CriacaoClientePFDTO(1234,
                UtilitariosGerais.removerFormatacaoCpfCnpj(faker.numerify("###########")), "nome", "email@email.com");

        final MvcResult resposta = mockMvc
                .perform(post("/clientes-pf").content(objectMapper.writeValueAsString(postBody))
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andReturn();

        final String responseBody = resposta.getResponse().getContentAsString();
        final RespostaCriacaoClientePFDTO clientePF = objectMapper.readValue(responseBody,
                RespostaCriacaoClientePFDTO.class);

        assertThat(this.repositorioClientePF.findAll(), hasSize(1));
        assertThat(UtilitariosGerais.removerFormatacaoCpfCnpj(clientePF.cpf()), equalTo(postBody.cpf()));
        assertThat(clientePF.nome(), equalTo(postBody.nome()));
    }

    @Test
    void criarErroUniqueCpfEmail() throws Exception {
        final ClientePF cliente1 = this.saveCliente();

        final Long cpf = UtilitariosGerais.removerFormatacaoCpfCnpj(cliente1.getCpf());
        final String email = cliente1.getEmail();

        final CriacaoClientePFDTO postBody = new CriacaoClientePFDTO(1234, cpf, "nome", email);

        mockMvc.perform(post("/clientes-pf").content(objectMapper.writeValueAsString(postBody))
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").doesNotExist())
                .andExpect(content().string(containsString("Ja existe um cliente PF para os dados fornecidos")));

        assertThat(this.repositorioClientePF.findAll(), hasSize(1));
    }

    @ParameterizedTest
    @MethodSource("fornecerArgumentosForIdInexistenteTest")
    void operacaoComIdInexistente(final String id, final MockHttpServletRequestBuilder requestBuilder)
            throws Exception {
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").doesNotExist())
                .andExpect(content().string(containsString("Cliente PF nao encontrado")));
    }

    @ParameterizedTest
    @MethodSource("fornecerArgumentosForIdInvalidoTest")
    void operacaoComIdInvalido(final String id, final MockHttpServletRequestBuilder requestBuilder) throws Exception {
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").doesNotExist())
                .andExpect(content().string(containsString("UUID invalido: " + id)));
    }

    private static Stream<Arguments> fornecerArgumentosForIdInexistenteTest() throws Exception {
        String id = UUID.randomUUID().toString();

        final String uri = "/clientes-pf/" + id;
        final ObjectMapper objectMapper = new ObjectMapper();
        final Faker faker = new Faker();

        final AtualizacaoClientePFDTO putBody = new AtualizacaoClientePFDTO(1234,
                UtilitariosGerais.removerFormatacaoCpfCnpj(faker.numerify("###########")), "nome", "email@email.com");

        return Stream.of(
                Arguments.of(id, get(uri)),
                Arguments.of(id,
                        put(uri).content(objectMapper.writeValueAsString(putBody)).contentType("application/json")),
                Arguments.of(id, delete(uri)));
    }

    private static Stream<Arguments> fornecerArgumentosForIdInvalidoTest() throws Exception {
        String id = UUID.randomUUID().toString();
        id = id.substring(0, id.length() - 1) + "g";

        final String uri = "/clientes-pf/" + id;
        final ObjectMapper objectMapper = new ObjectMapper();
        final Faker faker = new Faker();

        final AtualizacaoClientePFDTO putBody = new AtualizacaoClientePFDTO(1234,
                UtilitariosGerais.removerFormatacaoCpfCnpj(faker.numerify("###########")), "nome", "email@email.com");

        return Stream.of(
                Arguments.of(id, get(uri)),
                Arguments.of(id,
                        put(uri).content(objectMapper.writeValueAsString(putBody)).contentType("application/json")),
                Arguments.of(id, delete(uri)));
    }

    private ClientePF saveCliente() {
        return this.repositorioClientePF.save(this.fakeClientePF());
    }

    private void deleteClientes() {
        this.repositorioClientePF.deleteAll();
    }

    private ClientePF fakeClientePF() {
        return ClientePF.builder().cpf(faker.numerify("###########")).mcc(1234).nome("nome")
                .email(faker.internet().emailAddress())
                .build();
    }
}
