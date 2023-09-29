package com.challenge3.challenge3.controladoras.cliente_pj;

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

import com.challenge3.challenge3.dominio.cliente_pj.ClientePJ;
import com.challenge3.challenge3.dtos.cliente_pj.AtualizacaoClientePJDTO;
import com.challenge3.challenge3.dtos.cliente_pj.CriacaoClientePJDTO;
import com.challenge3.challenge3.dtos.cliente_pj.respostas.RespostaAtualizacaoClientePJDTO;
import com.challenge3.challenge3.dtos.cliente_pj.respostas.RespostaCriacaoClientePJDTO;
import com.challenge3.challenge3.dtos.cliente_pj.respostas.RespostaSelecaoClientePJDTO;
import com.challenge3.challenge3.repositorios.RepositorioClientePJ;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;

@SpringBootTest
@AutoConfigureMockMvc
class ControladoraClientePJTest {
        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @Autowired
        private RepositorioClientePJ repositorioClientePJ;

        private Faker faker = new Faker();

        @AfterEach
        void tearDown() {
                this.deleteClientes();
        }

        @Test
        void listarTodos() throws Exception {
                this.saveCliente();
                this.saveCliente();

                final MvcResult resposta = mockMvc.perform(get("/clientes-pj"))
                                .andDo(print())
                                .andExpect(status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.[*].id").isNotEmpty())
                                .andReturn();

                final String responseBody = resposta.getResponse().getContentAsString();
                final List<ClientePJ> clientesPJ = objectMapper.readValue(responseBody, List.class);

                assertThat(clientesPJ, hasSize((int) this.repositorioClientePJ.count()));
        }

        @Test
        void acharPorIdExistente() throws Exception {
                final UUID id = this.saveCliente().getId();

                final MvcResult resposta = mockMvc.perform(get("/clientes-pj/" + id))
                                .andDo(print())
                                .andExpect(status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                                .andReturn();

                final String responseBody = resposta.getResponse().getContentAsString();
                final RespostaSelecaoClientePJDTO clientePJ = objectMapper.readValue(responseBody,
                                RespostaSelecaoClientePJDTO.class);

                assertThat(clientePJ.id(), equalTo(id));
                assertThat(this.repositorioClientePJ.findAll(), hasSize(1));
        }

        @Test
        void removerPorIdExistente() throws Exception {
                final UUID id = this.saveCliente().getId();

                mockMvc.perform(delete("/clientes-pj/" + id))
                                .andDo(print())
                                .andExpect(status().isNoContent());

                assertThat(this.repositorioClientePJ.findAll(), hasSize(0));
        }

        @Test
        void atualizarSucesso() throws Exception {
                final ClientePJ clientePJ = this.saveCliente();
                final UUID id = clientePJ.getId();

                final AtualizacaoClientePJDTO putBody = new AtualizacaoClientePJDTO(
                                Long.parseLong(faker.numerify("##############")),
                                faker.name().fullName(),
                                1234,
                                Long.parseLong(faker.numerify("###########")),
                                "tester",
                                "test@teste.com");

                final MvcResult resposta = mockMvc
                                .perform(put("/clientes-pj/" + id).content(objectMapper.writeValueAsString(putBody))
                                                .contentType("application/json"))
                                .andDo(print())
                                .andExpect(status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                                .andReturn();

                final String responseBody = resposta.getResponse().getContentAsString();
                final RespostaAtualizacaoClientePJDTO clientePJAtualizado = objectMapper.readValue(responseBody,
                                RespostaAtualizacaoClientePJDTO.class);

                assertThat(this.repositorioClientePJ.findAll(), hasSize(1));
                assertThat(clientePJAtualizado.id(), equalTo(id));
                assertThat(Long.valueOf(clientePJAtualizado.cnpj()), equalTo(putBody.cnpj()));
                assertThat(Long.valueOf(clientePJAtualizado.cpfContato()), equalTo(putBody.cpfContato()));
                assertThat(clientePJAtualizado.razaoSocial(), equalTo(putBody.razaoSocial()));

        }

        @Test
        void atualizarErroUniqueCnpj() throws Exception {
                final ClientePJ cliente1 = this.saveCliente();
                final ClientePJ cliente2 = this.saveCliente();

                final UUID id = cliente2.getId();
                final Long cnpj = Long.parseLong(cliente1.getCnpj());

                final AtualizacaoClientePJDTO putBody = new AtualizacaoClientePJDTO(
                                cnpj,
                                faker.name().fullName(),
                                1234,
                                Long.parseLong(faker.numerify("###########")),
                                "tester",
                                "test@teste.com");

                mockMvc.perform(put("/clientes-pj/" + id).content(objectMapper.writeValueAsString(putBody))
                                .contentType("application/json"))
                                .andDo(print())
                                .andExpect(status().isConflict())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.id").doesNotExist())
                                .andExpect(content().string(
                                                containsString("Ja existe um cliente PJ para os dados fornecidos")));
        }

        @Test
        void criar() throws Exception {
                final CriacaoClientePJDTO postBody = new CriacaoClientePJDTO(
                                Long.parseLong(faker.numerify("##############")),
                                faker.name().fullName(),
                                1234,
                                Long.parseLong(faker.numerify("###########")),
                                "tester",
                                "test@teste.com");

                final MvcResult resposta = mockMvc
                                .perform(post("/clientes-pj").content(objectMapper.writeValueAsString(postBody))
                                                .contentType("application/json"))
                                .andDo(print())
                                .andExpect(status().isCreated())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                                .andReturn();

                final String responseBody = resposta.getResponse().getContentAsString();
                final RespostaCriacaoClientePJDTO clientePJ = objectMapper.readValue(responseBody,
                                RespostaCriacaoClientePJDTO.class);

                assertThat(this.repositorioClientePJ.findAll(), hasSize(1));
                assertThat(Long.valueOf(clientePJ.cnpj()), equalTo(postBody.cnpj()));
                assertThat(Long.valueOf(clientePJ.cpfContato()), equalTo(postBody.cpfContato()));
                assertThat(clientePJ.razaoSocial(), equalTo(postBody.razaoSocial()));
        }

        @Test
        void criarErroUniqueCnpj() throws Exception {
                final ClientePJ cliente1 = this.saveCliente();

                final Long cnpj = Long.parseLong(cliente1.getCnpj());

                final CriacaoClientePJDTO postBody = new CriacaoClientePJDTO(
                                cnpj,
                                faker.name().fullName(),
                                1234,
                                Long.parseLong(faker.numerify("###########")),
                                "tester",
                                "test@teste.com");

                mockMvc.perform(post("/clientes-pj").content(objectMapper.writeValueAsString(postBody))
                                .contentType("application/json"))
                                .andDo(print())
                                .andExpect(status().isConflict())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.id").doesNotExist())
                                .andExpect(content().string(
                                                containsString("Ja existe um cliente PJ para os dados fornecidos")));

                assertThat(this.repositorioClientePJ.findAll(), hasSize(1));
        }

        @ParameterizedTest
        @MethodSource("provideArgumentsForIdInexistenteTest")
        void operacaoComIdInexistente(final String id, final MockHttpServletRequestBuilder requestBuilder)
                        throws Exception {
                mockMvc.perform(requestBuilder)
                                .andDo(print())
                                .andExpect(status().isNotFound())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.id").doesNotExist())
                                .andExpect(content().string(containsString("Cliente PJ nao encontrado")));
        }

        @ParameterizedTest
        @MethodSource("provideArgumentsForIdInvalidoTest")
        void operacaoComIdInvalido(final String id, final MockHttpServletRequestBuilder requestBuilder)
                        throws Exception {
                mockMvc.perform(requestBuilder)
                                .andDo(print())
                                .andExpect(status().isBadRequest())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.id").doesNotExist())
                                .andExpect(content().string(containsString("UUID invalido: " + id)));
        }

        private static Stream<Arguments> provideArgumentsForIdInexistenteTest() throws Exception {
                String id = UUID.randomUUID().toString();

                final String uri = "/clientes-pj/" + id;
                final ObjectMapper objectMapper = new ObjectMapper();
                final Faker faker = new Faker();

                final AtualizacaoClientePJDTO putBody = new AtualizacaoClientePJDTO(
                                Long.parseLong(faker.numerify("##############")),
                                faker.name().fullName(),
                                1234,
                                Long.parseLong(faker.numerify("###########")),
                                "tester",
                                "test@teste.com");

                return Stream.of(
                                Arguments.of(id, get(uri)),
                                Arguments.of(id,
                                                put(uri).content(objectMapper.writeValueAsString(putBody))
                                                                .contentType("application/json")),
                                Arguments.of(id, delete(uri)));
        }

        private static Stream<Arguments> provideArgumentsForIdInvalidoTest() throws Exception {
                String id = UUID.randomUUID().toString();
                id = id.substring(0, id.length() - 1) + "g";

                final String uri = "/clientes-pj/" + id;
                final ObjectMapper objectMapper = new ObjectMapper();
                final Faker faker = new Faker();

                final AtualizacaoClientePJDTO putBody = new AtualizacaoClientePJDTO(
                                Long.parseLong(faker.numerify("##############")),
                                faker.name().fullName(),
                                1234,
                                Long.parseLong(faker.numerify("###########")),
                                "tester",
                                "test@teste.com");

                return Stream.of(
                                Arguments.of(id, get(uri)),
                                Arguments.of(id,
                                                put(uri).content(objectMapper.writeValueAsString(putBody))
                                                                .contentType("application/json")),
                                Arguments.of(id, delete(uri)));
        }

        private ClientePJ saveCliente() {
                return repositorioClientePJ.save(this.fakeClientePJ());
        }

        private void deleteClientes() {
                repositorioClientePJ.deleteAll();
        }

        private ClientePJ fakeClientePJ() {
                return ClientePJ.builder()
                                .cnpj(faker.numerify("##############"))
                                .razaoSocial(faker.name().fullName())
                                .mcc(1234)
                                .cpfContato("12345678901")
                                .nomeContato("tester")
                                .emailContato("test@teste.com")
                                .build();
        }

}