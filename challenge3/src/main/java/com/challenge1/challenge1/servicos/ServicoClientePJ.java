package com.challenge1.challenge1.servicos;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.challenge1.challenge1.dominio.cliente_pj.ClientePJ;
import com.challenge1.challenge1.dtos.cliente_pj.AtualizacaoClientePJDTO;
import com.challenge1.challenge1.dtos.cliente_pj.CriacaoClientePJDTO;
import com.challenge1.challenge1.dtos.cliente_pj.mapeadores.MapeadorAtualizacaoClientePJ;
import com.challenge1.challenge1.dtos.cliente_pj.mapeadores.MapeadorCriacaoClientePJ;
import com.challenge1.challenge1.dtos.cliente_pj.mapeadores.MapeadorSelecaoClientePJ;
import com.challenge1.challenge1.dtos.cliente_pj.respostas.RespostaAtualizacaoClientePJDTO;
import com.challenge1.challenge1.dtos.cliente_pj.respostas.RespostaCriacaoClientePJDTO;
import com.challenge1.challenge1.dtos.cliente_pj.respostas.RespostaSelecaoClientePJDTO;
import com.challenge1.challenge1.repositorios.RepositorioClientePJ;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class ServicoClientePJ {
    private static final String CLIENTE_NAO_ENCONTRADO_MENSAGEM_ERRO = "Cliente PJ nao encontrado";

    private final RepositorioClientePJ repositorio;
    private final MapeadorCriacaoClientePJ mapeadorCriacao;
    private final MapeadorAtualizacaoClientePJ mapeadorAtualizacao;
    private final MapeadorSelecaoClientePJ mapeadorSelecao;

    public RespostaCriacaoClientePJDTO criar(final CriacaoClientePJDTO criacaoClientePJDTO) {
        log.info("Criando cliente PJ: {}", criacaoClientePJDTO);
        return this.mapeadorCriacao
                .paraDto(this.repositorio.save(this.mapeadorCriacao.paraEntidade(criacaoClientePJDTO)));
    }

    public RespostaAtualizacaoClientePJDTO atualizar(
            final UUID id,
            final AtualizacaoClientePJDTO atualizacaoClientePJDTO) {
        this.acharEntidadePorId(id);
        final ClientePJ clientePJAtualizado = this.mapeadorAtualizacao.paraEntidade(id,
                atualizacaoClientePJDTO);

        log.info("Atualizando cliente PJ com id={}", id);
        return this.mapeadorAtualizacao.paraDto(this.repositorio.save(clientePJAtualizado));
    }

    public void remover(final UUID id) {
        final ClientePJ clientePJ = this.acharEntidadePorId(id);
        log.info("Removendo cliente PJ com id={}", id);
        this.repositorio.delete(clientePJ);
    }

    public RespostaSelecaoClientePJDTO acharPorId(final UUID id) {
        final ClientePJ clientePJ = this.acharEntidadePorId(id);
        log.info("Buscando cliente PJ com id={}", id);
        return this.mapeadorSelecao.paraDto(clientePJ);
    }

    public List<RespostaSelecaoClientePJDTO> listarTodos() {
        log.info("Listando todos os clientes PJ");
        return this.repositorio
                .findAll()
                .stream()
                .map(this.mapeadorSelecao::paraDto)
                .collect(Collectors.toList());
    }

    ClientePJ acharEntidadePorId(final UUID id) {
        return this.repositorio.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        CLIENTE_NAO_ENCONTRADO_MENSAGEM_ERRO));
    }
}
