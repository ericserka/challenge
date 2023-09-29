package com.challenge4.challenge4.servicos;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
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

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class ServicoClientePF {
    private static final String CLIENTE_NAO_ENCONTRADO_MENSAGEM_ERRO = "Cliente PF nao encontrado";

    private final RepositorioClientePF repositorio;
    private final MapeadorCriacaoClientePF mapeadorCriacao;
    private final MapeadorAtualizacaoClientePF mapeadorAtualizacao;
    private final MapeadorSelecaoClientePF mapeadorSelecao;
    private final ServicoFila servicoFila;

    public RespostaCriacaoClientePFDTO criar(final CriacaoClientePFDTO criacaoClientePFDTO) {
        log.info("Criando cliente PF: {}", criacaoClientePFDTO);

        final ClientePF clientePF = this.repositorio.save(this.mapeadorCriacao.paraEntidade(criacaoClientePFDTO));
        this.servicoFila.enfileirarClientePF(clientePF);

        return this.mapeadorCriacao.paraDto(clientePF);
    }

    public RespostaAtualizacaoClientePFDTO atualizar(
            final UUID id,
            final AtualizacaoClientePFDTO atualizacaoClientePFDTO) {
        this.acharEntidadePorId(id);
        log.info("Atualizando cliente PF com id={}", id);

        final ClientePF clientePFAtualizado = this.repositorio
                .save(this.mapeadorAtualizacao.paraEntidade(id, atualizacaoClientePFDTO));

        this.servicoFila.enfileirarClientePF(clientePFAtualizado);

        return this.mapeadorAtualizacao.paraDto(clientePFAtualizado);
    }

    public void remover(final UUID id) {
        final ClientePF clientePF = this.acharEntidadePorId(id);
        log.info("Removendo cliente PF com id={}", id);
        this.repositorio.delete(clientePF);
    }

    public RespostaSelecaoClientePFDTO acharPorId(final UUID id) {
        final ClientePF clientePF = this.acharEntidadePorId(id);
        log.info("Buscando cliente PF com id={}", id);
        return this.mapeadorSelecao.paraDto(clientePF);
    }

    public List<RespostaSelecaoClientePFDTO> listarTodos() {
        log.info("Listando todos os clientes PF");
        return this.repositorio
                .findAll()
                .stream()
                .map(this.mapeadorSelecao::paraDto)
                .collect(Collectors.toList());
    }

    ClientePF acharEntidadePorId(final UUID id) {
        return this.repositorio.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        CLIENTE_NAO_ENCONTRADO_MENSAGEM_ERRO));
    }
}
