package com.challenge1.challenge1.controladoras.cliente_pf;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.challenge1.challenge1.dtos.cliente_pf.AtualizacaoClientePFDTO;
import com.challenge1.challenge1.dtos.cliente_pf.CriacaoClientePFDTO;
import com.challenge1.challenge1.dtos.cliente_pf.respostas.RespostaAtualizacaoClientePFDTO;
import com.challenge1.challenge1.dtos.cliente_pf.respostas.RespostaCriacaoClientePFDTO;
import com.challenge1.challenge1.dtos.cliente_pf.respostas.RespostaSelecaoClientePFDTO;
import com.challenge1.challenge1.excecoes.CorpoRequisicaoInvalidoDTO;
import com.challenge1.challenge1.excecoes.ExcecaoDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Cliente PF")
public interface ControladoraClientePF {
        @Operation(summary = "Listar todos clientes PF", method = "GET")
        @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Clientes PF recuperados com sucesso"),
                        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(schema = @Schema(implementation = ExcecaoDTO.class), examples = {
                                        @ExampleObject(name = "", value = "{\"mensagem\": \"Erro interno do servidor\", \"codigoStatus\": 500}") })) })
        @GetMapping
        public ResponseEntity<List<RespostaSelecaoClientePFDTO>> listarTodos();

        @Operation(summary = "Retorna um cliente PF por ID", method = "GET")
        @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Cliente PF recuperado com sucesso"),
                        @ApiResponse(responseCode = "400", description = "UUID inválido", content = @Content(schema = @Schema(implementation = ExcecaoDTO.class), examples = {
                                        @ExampleObject(name = "", value = "{\"mensagem\": \"UUID invalido: uuid-invalido\", \"codigoStatus\": 400}") })),
                        @ApiResponse(responseCode = "404", description = "Cliente PF nao encontrado", content = @Content(schema = @Schema(implementation = ExcecaoDTO.class), examples = {
                                        @ExampleObject(name = "", value = "{\"mensagem\": \"Cliente PF nao encontrado\", \"codigoStatus\": 404}") })),
                        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(schema = @Schema(implementation = ExcecaoDTO.class), examples = {
                                        @ExampleObject(name = "", value = "{\"mensagem\": \"Erro interno do servidor\", \"codigoStatus\": 500}") })) })
        @GetMapping("/{id}")
        public ResponseEntity<RespostaSelecaoClientePFDTO> acharPorId(
                        @PathVariable final String id);

        @Operation(summary = "Remova um cliente PF existente por ID", method = "DELETE")
        @ApiResponses(value = { @ApiResponse(responseCode = "204", description = "Cliente PF removido com sucesso"),
                        @ApiResponse(responseCode = "400", description = "UUID invalido", content = @Content(schema = @Schema(implementation = ExcecaoDTO.class), examples = {
                                        @ExampleObject(name = "", value = "{\"mensagem\": \"UUID invalido: uuid-invalido\", \"codigoStatus\": 400}") })),
                        @ApiResponse(responseCode = "404", description = "Cliente PF nao encontrado", content = @Content(schema = @Schema(implementation = ExcecaoDTO.class), examples = {
                                        @ExampleObject(name = "", value = "{\"mensagem\": \"Cliente PF nao encontrado\", \"codigoStatus\": 404}") })),
                        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(schema = @Schema(implementation = ExcecaoDTO.class), examples = {
                                        @ExampleObject(name = "", value = "{\"mensagem\": \"Erro interno do servidor\", \"codigoStatus\": 500}") })) })
        @DeleteMapping("/{id}")
        @ResponseStatus(HttpStatus.NO_CONTENT)
        public void remover(@PathVariable final String id);

        @Operation(summary = "Atualize um cliente PF por ID", method = "PUT")
        @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Cliente PF atualizado com sucesso"),
                        @ApiResponse(responseCode = "400", description = "Corpo da requisicao invalido", content = @Content(schema = @Schema(oneOf = {
                                        CorpoRequisicaoInvalidoDTO.class, ExcecaoDTO.class }), examples = {
                                                        @ExampleObject(name = "Corpo da requisicao invalido #1", value = "{\"mensagem\": \"Corpo da requisicao invalido\", \"codigoStatus\": 400, \"erros\": {\"cpf\": \"nao deve ser nulo\"}}"),
                                                        @ExampleObject(name = "Corpo da requisicao invalido #2", value = "{\"mensagem\": \"JSON parse error: Cannot deserialize value of type `java.lang.Integer` from String \\\"abc\\\": not a valid `java.lang.Integer` value\", \"codigoStatus\": 400}"),
                                                        @ExampleObject(name = "UUID invalido", value = "{\"mensagem\": \"UUID invalido: uuid-invalido\", \"codigoStatus\": 400}") })),
                        @ApiResponse(responseCode = "404", description = "Cliente PF nao encontrado", content = @Content(schema = @Schema(implementation = ExcecaoDTO.class), examples = {
                                        @ExampleObject(name = "", value = "{\"mensagem\": \"Cliente PF nao encontrado\", \"codigoStatus\": 404}") })),
                        @ApiResponse(responseCode = "409", description = "Ja existe um cliente PF para os dados fornecidos", content = @Content(schema = @Schema(implementation = ExcecaoDTO.class), examples = {
                                        @ExampleObject(name = "", value = "{\"mensagem\": \"Ja existe um cliente PF para os dados fornecidos\", \"codigoStatus\": 409}") })),
                        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(schema = @Schema(implementation = ExcecaoDTO.class), examples = {
                                        @ExampleObject(name = "", value = "{\"mensagem\": \"Erro interno do servidor\", \"codigoStatus\": 500}") })) })
        @PutMapping(path = "/{id}", consumes = "application/json")
        public ResponseEntity<RespostaAtualizacaoClientePFDTO> atualizar(
                        @PathVariable final String id,
                        @RequestBody @Valid final AtualizacaoClientePFDTO atualizacaoClientePFDTO);

        @Operation(summary = "Crie um novo cliente PF", method = "POST")
        @ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Cliente PF criado com sucesso"),
                        @ApiResponse(responseCode = "400", description = "Corpo da requisicao invalido", content = @Content(schema = @Schema(oneOf = {
                                        CorpoRequisicaoInvalidoDTO.class, ExcecaoDTO.class }), examples = {
                                                        @ExampleObject(name = "Corpo da requisicao invalido #1", value = "{\"mensagem\": \"Corpo da requisicao invalido\", \"codigoStatus\": 400, \"erros\": {\"cpf\": \"nao deve ser nulo\"}}"),
                                                        @ExampleObject(name = "Corpo da requisicao invalido #2", value = "{\"mensagem\": \"JSON parse error: Cannot deserialize value of type `java.lang.Integer` from String \\\"abc\\\": not a valid `java.lang.Integer` value\", \"codigoStatus\": 400}") })),
                        @ApiResponse(responseCode = "409", description = "Ja existe um cliente PF para os dados fornecidos", content = @Content(schema = @Schema(implementation = ExcecaoDTO.class), examples = {
                                        @ExampleObject(name = "", value = "{\"mensagem\": \"Ja existe um cliente PF para os dados fornecidos\", \"codigoStatus\": 409}") })),
                        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(schema = @Schema(implementation = ExcecaoDTO.class), examples = {
                                        @ExampleObject(name = "", value = "{\"mensagem\": \"Erro interno do servidor\", \"codigoStatus\": 500}") })) })
        @PostMapping(consumes = "application/json")
        public ResponseEntity<RespostaCriacaoClientePFDTO> criar(
                        @RequestBody @Valid final CriacaoClientePFDTO criacaoClientePFDTO);
}
