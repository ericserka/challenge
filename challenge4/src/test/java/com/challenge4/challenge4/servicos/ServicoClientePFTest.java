package com.challenge4.challenge4.servicos;

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

import com.challenge4.challenge4.dominio.cliente_pf.ClientePF;
import com.challenge4.challenge4.dtos.cliente_pf.AtualizacaoClientePFDTO;
import com.challenge4.challenge4.dtos.cliente_pf.CriacaoClientePFDTO;
import com.challenge4.challenge4.dtos.cliente_pf.mapeadores.MapeadorAtualizacaoClientePF;
import com.challenge4.challenge4.dtos.cliente_pf.mapeadores.MapeadorCriacaoClientePF;
import com.challenge4.challenge4.dtos.cliente_pf.mapeadores.MapeadorSelecaoClientePF;
import com.challenge4.challenge4.dtos.cliente_pf.respostas.RespostaAtualizacaoClientePFDTO;
import com.challenge4.challenge4.dtos.cliente_pf.respostas.RespostaCriacaoClientePFDTO;
import com.challenge4.challenge4.dtos.cliente_pf.respostas.RespostaSelecaoClientePFDTO;
import com.challenge4.challenge4.repositorios.RepositorioClientePF;
import com.challenge4.challenge4.servicos.ServicoClientePF;
import com.challenge4.challenge4.servicos.ServicoFila;
import com.challenge4.challenge4.utilitarios.UtilitariosGerais;

@ExtendWith(MockitoExtension.class)
public class ServicoClientePFTest {
        @Mock
        private RepositorioClientePF repositorio;

        @Mock
        private MapeadorCriacaoClientePF mapeadorCriacao;
        @Mock
        private MapeadorAtualizacaoClientePF mapeadorAtualizacao;
        @Mock
        private MapeadorSelecaoClientePF mapeadorSelecao;
        @Mock
        private ServicoFila servicoFila;

        @InjectMocks
        private ServicoClientePF servico;

        @Test
        void acharEntidadePorId_quandoClientePFNaoExiste_entaoRetornaErro() {
                UUID id = UUID.randomUUID();
                Throwable thrown = assertThrows(ResponseStatusException.class,
                                () -> this.servico.acharEntidadePorId(id));

                assertThat(thrown.getMessage(), is("404 NOT_FOUND \"Cliente PF nao encontrado\""));
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
        void acharPorId_quandoClientePFExiste_entaoRetornoValido() {
                final ClientePF clientePF = new ClientePF("01234567890", 1234, "nome", "email@email.com");
                final UUID id = clientePF.getId();
                final RespostaSelecaoClientePFDTO respostaSelecaoClientePFDTO = new RespostaSelecaoClientePFDTO(id,
                                clientePF.getCpf(), clientePF.getNome(), clientePF.getMcc(), clientePF.getEmail(),
                                clientePF.getCriadoEm(), clientePF.getAtualizadoEm());

                when(this.repositorio.findById(id)).thenReturn(Optional.of(clientePF));
                when(this.mapeadorSelecao.paraDto(clientePF)).thenReturn(respostaSelecaoClientePFDTO);

                final RespostaSelecaoClientePFDTO resultado = this.servico.acharPorId(id);

                verify(this.repositorio, times(1)).findById(id);
                verify(this.mapeadorSelecao, times(1)).paraDto(clientePF);

                assertThat(resultado, equalTo(respostaSelecaoClientePFDTO));
        }

        @Test
        void atualizar_quandoClientePFExiste_entaoRetornoValido() {
                final ClientePF clientePF = new ClientePF("01234567890", 1234, "nome", "email@email.com");
                final UUID id = clientePF.getId();

                final AtualizacaoClientePFDTO atualizacaoClientePFDTO = new AtualizacaoClientePFDTO(4321, 9876543210L,
                                "novo nome", "novoEmail@email.com");

                final ClientePF clientePFAtualizado = new ClientePF(
                                UtilitariosGerais.formatarCpf(atualizacaoClientePFDTO.cpf()),
                                atualizacaoClientePFDTO.mcc(),
                                atualizacaoClientePFDTO.nome(), atualizacaoClientePFDTO.email());

                final RespostaAtualizacaoClientePFDTO respostaAtualizacaoClientePFDTO = new RespostaAtualizacaoClientePFDTO(
                                clientePFAtualizado.getId(), clientePFAtualizado.getCpf(),
                                clientePFAtualizado.getNome(),
                                clientePFAtualizado.getMcc(), clientePFAtualizado.getEmail(),
                                clientePFAtualizado.getCriadoEm(),
                                clientePFAtualizado.getAtualizadoEm());

                when(this.repositorio.findById(id)).thenReturn(Optional.of(clientePF));
                when(this.mapeadorAtualizacao.paraEntidade(id, atualizacaoClientePFDTO))
                                .thenReturn(clientePFAtualizado);
                when(this.repositorio.save(clientePFAtualizado)).thenReturn(clientePFAtualizado);
                doNothing().when(this.servicoFila).enfileirarClientePF(clientePFAtualizado);

                when(this.mapeadorAtualizacao.paraDto(clientePFAtualizado)).thenReturn(respostaAtualizacaoClientePFDTO);

                final RespostaAtualizacaoClientePFDTO resultado = this.servico.atualizar(id, atualizacaoClientePFDTO);

                verify(this.repositorio, times(1)).findById(id);
                verify(this.mapeadorAtualizacao, times(1)).paraEntidade(id, atualizacaoClientePFDTO);
                verify(this.repositorio, times(1)).save(clientePFAtualizado);
                verify(this.mapeadorAtualizacao, times(1)).paraDto(clientePFAtualizado);

                assertThat(resultado, equalTo(respostaAtualizacaoClientePFDTO));
        }

        @Test
        void atualizar_quandoDadosRepetidos_entaoRetornaErro() {
                final ClientePF clientePFASerAtualizado = new ClientePF("01234567890", 1234, "nome", "email@email.com");
                final UUID id = clientePFASerAtualizado.getId();

                final ClientePF clientePFExistente = new ClientePF("09876543210", 4321, "nome 2", "email2@email.com");

                final AtualizacaoClientePFDTO atualizacaoClientePFDTO = new AtualizacaoClientePFDTO(
                                clientePFExistente.getMcc(),
                                UtilitariosGerais.removerFormatacaoCpfCnpj(clientePFExistente.getCpf()),
                                clientePFExistente.getNome(),
                                clientePFExistente.getEmail());

                when(this.repositorio.findById(id)).thenReturn(Optional.of(clientePFASerAtualizado));
                when(this.mapeadorAtualizacao.paraEntidade(id, atualizacaoClientePFDTO))
                                .thenReturn(clientePFExistente);
                when(this.repositorio.save(clientePFExistente)).thenThrow(DataIntegrityViolationException.class);

                assertThrows(DataIntegrityViolationException.class,
                                () -> this.servico.atualizar(id, atualizacaoClientePFDTO));
        }

        @Test
        void remover_quandoClientePFExiste_entaoRetornoValido() {
                final ClientePF clientePF = new ClientePF("01234567890", 1234, "nome", "email@email.com");
                final UUID id = clientePF.getId();

                when(this.repositorio.findById(id)).thenReturn(Optional.of(clientePF));

                this.servico.remover(id);

                verify(this.repositorio, times(1)).delete(clientePF);
        }

        @Test
        void listarTodosClientePF() {
                final ClientePF clientePF1 = new ClientePF("01234567890", 1234, "nome", "email@email.com");

                final ClientePF clientePF2 = new ClientePF("09876543210", 4321, "nome 2", "email2@email.com");

                final RespostaSelecaoClientePFDTO respostaSelecaoClientePFDTO1 = new RespostaSelecaoClientePFDTO(
                                clientePF1.getId(), clientePF1.getCpf(), clientePF1.getNome(), clientePF1.getMcc(),
                                clientePF1.getEmail(), clientePF1.getCriadoEm(), clientePF1.getAtualizadoEm());
                final RespostaSelecaoClientePFDTO respostaSelecaoClientePFDTO2 = new RespostaSelecaoClientePFDTO(
                                clientePF2.getId(), clientePF2.getCpf(), clientePF2.getNome(), clientePF2.getMcc(),
                                clientePF2.getEmail(), clientePF2.getCriadoEm(), clientePF2.getAtualizadoEm());

                final List<ClientePF> clientesPF = Arrays.asList(clientePF1, clientePF2);

                final List<RespostaSelecaoClientePFDTO> clientesPFDTOs = Arrays.asList(respostaSelecaoClientePFDTO1,
                                respostaSelecaoClientePFDTO2);

                when(this.repositorio.findAll()).thenReturn(clientesPF);
                when(this.mapeadorSelecao.paraDto(clientePF1)).thenReturn(respostaSelecaoClientePFDTO1);
                when(this.mapeadorSelecao.paraDto(clientePF2)).thenReturn(respostaSelecaoClientePFDTO2);

                final List<RespostaSelecaoClientePFDTO> resultado = this.servico.listarTodos();

                verify(this.repositorio, times(1)).findAll();

                assertThat(resultado, equalTo(clientesPFDTOs));
        }

        @Test
        void criar_quandoPrimeiroCadastro_entaoRetornoValido() {
                final CriacaoClientePFDTO criacaoClientePFDTO = new CriacaoClientePFDTO(1234, 1234567890L, "nome",
                                "email@email.com");

                final ClientePF clientePFCriado = new ClientePF(
                                UtilitariosGerais.formatarCpf(criacaoClientePFDTO.cpf()),
                                criacaoClientePFDTO.mcc(), criacaoClientePFDTO.nome(), criacaoClientePFDTO.email());

                final RespostaCriacaoClientePFDTO respostaCriacaoClientePFDTO = new RespostaCriacaoClientePFDTO(
                                clientePFCriado.getId(), clientePFCriado.getCpf(), clientePFCriado.getNome(),
                                clientePFCriado.getMcc(),
                                clientePFCriado.getEmail(), clientePFCriado.getCriadoEm());

                when(this.mapeadorCriacao.paraEntidade(criacaoClientePFDTO)).thenReturn(clientePFCriado);
                when(this.repositorio.save(clientePFCriado)).thenReturn(clientePFCriado);
                doNothing().when(this.servicoFila).enfileirarClientePF(clientePFCriado);

                when(this.mapeadorCriacao.paraDto(clientePFCriado)).thenReturn(respostaCriacaoClientePFDTO);

                final RespostaCriacaoClientePFDTO resultado = this.servico.criar(criacaoClientePFDTO);

                assertThat(resultado, equalTo(respostaCriacaoClientePFDTO));
        }

        @Test
        void criar_quandoCadastroJaExistente_entaoRetornaErro() {
                final ClientePF clientePFExistente = new ClientePF("01234567890", 1234, "nome", "email@email.com");

                final CriacaoClientePFDTO criacaoClientePFDTO = new CriacaoClientePFDTO(clientePFExistente.getMcc(),
                                UtilitariosGerais.removerFormatacaoCpfCnpj(clientePFExistente.getCpf()),
                                clientePFExistente.getNome(),
                                clientePFExistente.getEmail());

                when(this.mapeadorCriacao.paraEntidade(criacaoClientePFDTO)).thenReturn(clientePFExistente);
                when(this.repositorio.save(clientePFExistente)).thenThrow(DataIntegrityViolationException.class);

                assertThrows(DataIntegrityViolationException.class,
                                () -> this.servico.criar(criacaoClientePFDTO));
        }
}
