package com.challenge3.challenge3.servicos;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.server.ResponseStatusException;

import com.challenge3.challenge3.dominio.cliente_pj.ClientePJ;
import com.challenge3.challenge3.dtos.cliente_pj.AtualizacaoClientePJDTO;
import com.challenge3.challenge3.dtos.cliente_pj.CriacaoClientePJDTO;
import com.challenge3.challenge3.dtos.cliente_pj.mapeadores.MapeadorAtualizacaoClientePJ;
import com.challenge3.challenge3.dtos.cliente_pj.mapeadores.MapeadorCriacaoClientePJ;
import com.challenge3.challenge3.dtos.cliente_pj.mapeadores.MapeadorSelecaoClientePJ;
import com.challenge3.challenge3.dtos.cliente_pj.respostas.RespostaAtualizacaoClientePJDTO;
import com.challenge3.challenge3.dtos.cliente_pj.respostas.RespostaCriacaoClientePJDTO;
import com.challenge3.challenge3.dtos.cliente_pj.respostas.RespostaSelecaoClientePJDTO;
import com.challenge3.challenge3.repositorios.RepositorioClientePJ;
import com.challenge3.challenge3.utilitarios.UtilitariosGerais;

@ExtendWith(MockitoExtension.class)
class ServicoClientePJTest {
        @Mock
        private RepositorioClientePJ repositorio;

        @Mock
        private MapeadorCriacaoClientePJ mapeadorCriacao;
        @Mock
        private MapeadorAtualizacaoClientePJ mapeadorAtualizacao;
        @Mock
        private MapeadorSelecaoClientePJ mapeadorSelecao;
        @Mock
        private ServicoFila servicoFila;

        @InjectMocks
        private ServicoClientePJ servico;

        @Test
        void acharEntidadePorId_quandoClientePJNaoExiste_entaoRetornaErro() {
                UUID id = UUID.randomUUID();
                Throwable thrown = assertThrows(ResponseStatusException.class,
                                () -> this.servico.acharEntidadePorId(id));

                assertThat(thrown.getMessage(), is("404 NOT_FOUND \"Cliente PJ nao encontrado\""));
        }

        @Test
        void acharEntidadePorId_quandoUUIDInvalido_entaoRetornaErro() {
                String id = "uuid-invalido";
                Throwable thrown = assertThrows(IllegalArgumentException.class,
                                () -> this.servico.acharEntidadePorId(UUID.fromString(id)));

                assertThat(thrown.getMessage(), is("Invalid UUID string: " + id));

                String outroId = "gggggggg-gggg-gggg-gggg-gggggggggggg";
                assertThrows(NumberFormatException.class,
                                () -> this.servico.acharEntidadePorId(UUID.fromString(outroId)));
        }

        @Test
        void acharPorId_quandoClientePJExiste_entaoRetornoValido() {
                final ClientePJ clientePJ = new ClientePJ(
                                "12345678901234",
                                "razao social",
                                1234,
                                "12345678901",
                                "nomeContato",
                                "email@email.com");
                final UUID id = clientePJ.getId();
                final RespostaSelecaoClientePJDTO respostaSelecaoClientePJDTO = new RespostaSelecaoClientePJDTO(
                                clientePJ.getId(),
                                clientePJ.getCnpj(),
                                clientePJ.getRazaoSocial(),
                                clientePJ.getMcc(),
                                clientePJ.getCpfContato(),
                                clientePJ.getNomeContato(),
                                clientePJ.getEmailContato(),
                                clientePJ.getCriadoEm(),
                                clientePJ.getAtualizadoEm());

                when(this.repositorio.findById(id)).thenReturn(Optional.of(clientePJ));
                when(this.mapeadorSelecao.paraDto(clientePJ)).thenReturn(respostaSelecaoClientePJDTO);

                final RespostaSelecaoClientePJDTO resultado = this.servico.acharPorId(id);

                verify(this.repositorio, times(1)).findById(id);
                verify(this.mapeadorSelecao, times(1)).paraDto(clientePJ);

                assertThat(resultado, equalTo(respostaSelecaoClientePJDTO));
        }

        @Test
        void atualizar_quandoClientePJExiste_entaoRetornoValido() {
                final ClientePJ clientePJ = new ClientePJ(
                                "01234567890123",
                                "razao social",
                                1234,
                                "01234567890",
                                "nomeContato",
                                "email@email.com");
                final UUID id = clientePJ.getId();

                final ClientePJ clientePJAtualizado = new ClientePJ(
                                "03210987654321",
                                "nova razao social",
                                4321,
                                "09876543210",
                                "novo nome",
                                "novoEmail@email.com");

                final AtualizacaoClientePJDTO atualizacaoClientePJDTO = new AtualizacaoClientePJDTO(3210987654321L,
                                "nova razao social", 4321, 9876543210L, "novo nome", "novoEmail@email.com");

                final RespostaAtualizacaoClientePJDTO respostaAtualizacaoClientePJDTO = new RespostaAtualizacaoClientePJDTO(
                                clientePJAtualizado.getId(),
                                clientePJAtualizado.getCnpj(),
                                clientePJAtualizado.getRazaoSocial(),
                                clientePJAtualizado.getMcc(),
                                clientePJAtualizado.getCpfContato(),
                                clientePJAtualizado.getNomeContato(),
                                clientePJAtualizado.getEmailContato(),
                                clientePJAtualizado.getCriadoEm(),
                                clientePJAtualizado.getAtualizadoEm());

                when(this.repositorio.findById(id)).thenReturn(Optional.of(clientePJ));
                when(this.mapeadorAtualizacao.paraEntidade(id, atualizacaoClientePJDTO))
                                .thenReturn(clientePJAtualizado);
                when(this.repositorio.save(clientePJAtualizado)).thenReturn(clientePJAtualizado);
                doNothing().when(this.servicoFila).enfileirarClientePJ(clientePJAtualizado);

                when(this.mapeadorAtualizacao.paraDto(clientePJAtualizado)).thenReturn(respostaAtualizacaoClientePJDTO);

                final RespostaAtualizacaoClientePJDTO resultado = this.servico.atualizar(id, atualizacaoClientePJDTO);

                verify(this.repositorio, times(1)).findById(id);
                verify(this.mapeadorAtualizacao, times(1)).paraEntidade(id, atualizacaoClientePJDTO);
                verify(this.repositorio, times(1)).save(clientePJAtualizado);
                verify(this.mapeadorAtualizacao, times(1)).paraDto(clientePJAtualizado);

                assertThat(resultado, equalTo(respostaAtualizacaoClientePJDTO));
        }

        @Test
        void atualizar_quandoDadosRepetidos_entaoRetornaErro() {
                final ClientePJ clientePJASerAtualizado = new ClientePJ(
                                "01234567890123",
                                "razao social",
                                1234,
                                "01234567890",
                                "nomeContato",
                                "email@email.com");
                final UUID id = clientePJASerAtualizado.getId();

                final ClientePJ clientePJExistente = new ClientePJ(
                                "03210987654321",
                                "nova razao social",
                                4321,
                                "09876543210",
                                "novo nome",
                                "novoEmail@email.com");

                final AtualizacaoClientePJDTO atualizacaoClientePJDTO = new AtualizacaoClientePJDTO(3210987654321L,
                                "nova razao social", 4321, 9876543210L, "novo nome", "novoEmail@email.com");

                when(this.repositorio.findById(id)).thenReturn(Optional.of(clientePJASerAtualizado));
                when(this.mapeadorAtualizacao.paraEntidade(id, atualizacaoClientePJDTO))
                                .thenReturn(clientePJExistente);
                when(this.repositorio.save(clientePJExistente)).thenThrow(DataIntegrityViolationException.class);

                assertThrows(DataIntegrityViolationException.class,
                                () -> this.servico.atualizar(id, atualizacaoClientePJDTO));
        }

        @Test
        void remover_quandoClientePJExiste_entaoRetornoValido() {
                final ClientePJ clientePJ = new ClientePJ(
                                "01234567890123",
                                "razao social",
                                1234,
                                "01234567890",
                                "nomeContato",
                                "email@email.com");
                final UUID id = clientePJ.getId();

                when(this.repositorio.findById(id)).thenReturn(Optional.of(clientePJ));

                this.servico.remover(id);

                verify(this.repositorio, times(1)).delete(clientePJ);
        }

        @Test
        void listarTodosClientePJ() {
                final ClientePJ clientePJ1 = new ClientePJ(
                                "01234567890123",
                                "razao social 1",
                                1234,
                                "01234567890",
                                "nomeContato",
                                "email@email.com");

                final ClientePJ clientePJ2 = new ClientePJ(
                                "09876543210987",
                                "razao social 2",
                                1234,
                                "01234567890",
                                "nomeContato",
                                "email@email.com");

                final RespostaSelecaoClientePJDTO respostaSelecaoClientePJDTO1 = new RespostaSelecaoClientePJDTO(
                                clientePJ1.getId(), clientePJ1.getCnpj(), clientePJ1.getRazaoSocial(),
                                clientePJ1.getMcc(), clientePJ1.getCpfContato(), clientePJ1.getNomeContato(),
                                clientePJ1.getEmailContato(), clientePJ1.getCriadoEm(), clientePJ1.getAtualizadoEm());
                final RespostaSelecaoClientePJDTO respostaSelecaoClientePJDTO2 = new RespostaSelecaoClientePJDTO(
                                clientePJ2.getId(), clientePJ2.getCnpj(), clientePJ2.getRazaoSocial(),
                                clientePJ2.getMcc(), clientePJ2.getCpfContato(), clientePJ2.getNomeContato(),
                                clientePJ2.getEmailContato(), clientePJ2.getCriadoEm(), clientePJ2.getAtualizadoEm());

                final List<ClientePJ> clientesPJ = Arrays.asList(clientePJ1, clientePJ2);

                final List<RespostaSelecaoClientePJDTO> clientesPJDTOs = Arrays.asList(respostaSelecaoClientePJDTO1,
                                respostaSelecaoClientePJDTO2);

                when(this.repositorio.findAll()).thenReturn(clientesPJ);
                when(this.mapeadorSelecao.paraDto(clientePJ1)).thenReturn(respostaSelecaoClientePJDTO1);
                when(this.mapeadorSelecao.paraDto(clientePJ2)).thenReturn(respostaSelecaoClientePJDTO2);

                final List<RespostaSelecaoClientePJDTO> resultado = this.servico.listarTodos();

                verify(this.repositorio, times(1)).findAll();

                assertThat(resultado, equalTo(clientesPJDTOs));
        }

        @Test
        void criar_quandoPrimeiroCadastro_entaoRetornoValido() {
                final CriacaoClientePJDTO criacaoClientePJDTO = new CriacaoClientePJDTO(3210987654321L,
                                "razao social", 4321, 9876543210L, "nomeContato", "email@email.com");

                final ClientePJ clientePJCriado = new ClientePJ(
                                UtilitariosGerais.formatarCnpj(criacaoClientePJDTO.cnpj()),
                                criacaoClientePJDTO.razaoSocial(), criacaoClientePJDTO.mcc(),
                                UtilitariosGerais.formatarCpf(criacaoClientePJDTO.cpfContato()),
                                criacaoClientePJDTO.nomeContato(), criacaoClientePJDTO.emailContato());

                final RespostaCriacaoClientePJDTO respostaCriacaoClientePJDTO = new RespostaCriacaoClientePJDTO(
                                clientePJCriado.getId(), UtilitariosGerais.formatarCnpj(criacaoClientePJDTO.cnpj()),
                                criacaoClientePJDTO.razaoSocial(), criacaoClientePJDTO.mcc(),
                                UtilitariosGerais.formatarCpf(criacaoClientePJDTO.cpfContato()),
                                criacaoClientePJDTO.nomeContato(), criacaoClientePJDTO.emailContato(),
                                clientePJCriado.getCriadoEm());

                when(this.mapeadorCriacao.paraEntidade(criacaoClientePJDTO)).thenReturn(clientePJCriado);
                when(this.repositorio.save(clientePJCriado)).thenReturn(clientePJCriado);
                doNothing().when(this.servicoFila).enfileirarClientePJ(clientePJCriado);

                when(this.mapeadorCriacao.paraDto(clientePJCriado)).thenReturn(respostaCriacaoClientePJDTO);

                final RespostaCriacaoClientePJDTO resultado = this.servico.criar(criacaoClientePJDTO);

                assertThat(resultado, equalTo(respostaCriacaoClientePJDTO));
        }

        @Test
        void criar_quandoCadastroJaExistente_entaoRetornaErro() {
                final ClientePJ clientePJExistente = new ClientePJ(
                                "03210987654321",
                                "razao social",
                                4321,
                                "09876543210",
                                "nome",
                                "email@email.com");

                final CriacaoClientePJDTO criacaoClientePJDTO = new CriacaoClientePJDTO(
                                UtilitariosGerais.removerFormatacaoCpfCnpj(clientePJExistente.getCnpj()),
                                clientePJExistente.getRazaoSocial(), clientePJExistente.getMcc(),
                                UtilitariosGerais.removerFormatacaoCpfCnpj(clientePJExistente.getCpfContato()),
                                clientePJExistente.getNomeContato(), clientePJExistente.getEmailContato());

                when(this.mapeadorCriacao.paraEntidade(criacaoClientePJDTO)).thenReturn(clientePJExistente);
                when(this.repositorio.save(clientePJExistente)).thenThrow(DataIntegrityViolationException.class);

                assertThrows(DataIntegrityViolationException.class,
                                () -> this.servico.criar(criacaoClientePJDTO));
        }
}
