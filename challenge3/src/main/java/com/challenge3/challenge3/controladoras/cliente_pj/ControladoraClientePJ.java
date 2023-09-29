package com.challenge3.challenge3.controladoras.cliente_pj;

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

import com.challenge3.challenge3.dtos.cliente_pj.AtualizacaoClientePJDTO;
import com.challenge3.challenge3.dtos.cliente_pj.CriacaoClientePJDTO;
import com.challenge3.challenge3.dtos.cliente_pj.respostas.RespostaAtualizacaoClientePJDTO;
import com.challenge3.challenge3.dtos.cliente_pj.respostas.RespostaCriacaoClientePJDTO;
import com.challenge3.challenge3.dtos.cliente_pj.respostas.RespostaSelecaoClientePJDTO;
import com.challenge3.challenge3.excecoes.CorpoRequisicaoInvalidoDTO;
import com.challenge3.challenge3.excecoes.ExcecaoDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Cliente PJ")
public interface ControladoraClientePJ {
        @Operation(summary = "Listar todos clientes PJ", method = "GET")
        @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Clientes PJ recuperados com sucesso"),
                        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(schema = @Schema(implementation = ExcecaoDTO.class), examples = {
                                        @ExampleObject(name = "", value = "{\"mensagem\": \"Erro interno do servidor\", \"codigoStatus\": 500}") })) })
        @GetMapping
        public ResponseEntity<List<RespostaSelecaoClientePJDTO>> listarTodos();

        @Operation(summary = "Retorna um cliente PJ por ID", method = "GET")
        @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Cliente PJ recuperado com sucesso"),
                        @ApiResponse(responseCode = "400", description = "UUID inválido", content = @Content(schema = @Schema(implementation = ExcecaoDTO.class), examples = {
                                        @ExampleObject(name = "", value = "{\"mensagem\": \"UUID invalido: uuid-invalido\", \"codigoStatus\": 400}") })),
                        @ApiResponse(responseCode = "404", description = "Cliente PJ nao encontrado", content = @Content(schema = @Schema(implementation = ExcecaoDTO.class), examples = {
                                        @ExampleObject(name = "", value = "{\"mensagem\": \"Cliente PJ nao encontrado\", \"codigoStatus\": 404}") })),
                        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(schema = @Schema(implementation = ExcecaoDTO.class), examples = {
                                        @ExampleObject(name = "", value = "{\"mensagem\": \"Erro interno do servidor\", \"codigoStatus\": 500}") })) })
        @GetMapping("/{id}")
        public ResponseEntity<RespostaSelecaoClientePJDTO> acharPorId(
                        @PathVariable final String id);

        @Operation(summary = "Remova um cliente PJ existente por ID", method = "DELETE")
        @ApiResponses(value = { @ApiResponse(responseCode = "204", description = "Cliente PJ removido com sucesso"),
                        @ApiResponse(responseCode = "400", description = "UUID invalido", content = @Content(schema = @Schema(implementation = ExcecaoDTO.class), examples = {
                                        @ExampleObject(name = "", value = "{\"mensagem\": \"UUID invalido: uuid-invalido\", \"codigoStatus\": 400}") })),
                        @ApiResponse(responseCode = "404", description = "Cliente PJ nao encontrado", content = @Content(schema = @Schema(implementation = ExcecaoDTO.class), examples = {
                                        @ExampleObject(name = "", value = "{\"mensagem\": \"Cliente PJ nao encontrado\", \"codigoStatus\": 404}") })),
                        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(schema = @Schema(implementation = ExcecaoDTO.class), examples = {
                                        @ExampleObject(name = "", value = "{\"mensagem\": \"Erro interno do servidor\", \"codigoStatus\": 500}") })) })
        @DeleteMapping("/{id}")
        @ResponseStatus(HttpStatus.NO_CONTENT)
        public void remover(@PathVariable final String id);

        @Operation(summary = "Atualize um cliente PJ por ID", method = "PUT")
        @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Cliente PJ atualizado com sucesso"),
                        @ApiResponse(responseCode = "400", description = "Corpo da requisicao invalido", content = @Content(schema = @Schema(oneOf = {
                                        CorpoRequisicaoInvalidoDTO.class, ExcecaoDTO.class }), examples = {
                                                        @ExampleObject(name = "Corpo da requisicao invalido #1", value = "{\"mensagem\": \"Corpo da requisicao invalido\", \"codigoStatus\": 400, \"erros\": {\"cnpj\": \"nao deve ser nulo\"}}"),
                                                        @ExampleObject(name = "Corpo da requisicao invalido #2", value = "{\"mensagem\": \"JSON parse error: Cannot deserialize value of type `java.lang.Integer` from String \\\"abc\\\": not a valid `java.lang.Integer` value\", \"codigoStatus\": 400}"),
                                                        @ExampleObject(name = "UUID invalido", value = "{\"mensagem\": \"UUID invalido: uuid-invalido\", \"codigoStatus\": 400}") })),
                        @ApiResponse(responseCode = "404", description = "Cliente PJ nao encontrado", content = @Content(schema = @Schema(implementation = ExcecaoDTO.class), examples = {
                                        @ExampleObject(name = "", value = "{\"mensagem\": \"Cliente PJ nao encontrado\", \"codigoStatus\": 404}") })),
                        @ApiResponse(responseCode = "409", description = "Ja existe um cliente PJ para os dados fornecidos", content = @Content(schema = @Schema(implementation = ExcecaoDTO.class), examples = {
                                        @ExampleObject(name = "", value = "{\"mensagem\": \"Ja existe um cliente PJ para os dados fornecidos\", \"codigoStatus\": 409}") })),
                        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(schema = @Schema(implementation = ExcecaoDTO.class), examples = {
                                        @ExampleObject(name = "", value = "{\"mensagem\": \"Erro interno do servidor\", \"codigoStatus\": 500}") })) })
        @PutMapping(path = "/{id}", consumes = "application/json")
        public ResponseEntity<RespostaAtualizacaoClientePJDTO> atualizar(
                        @PathVariable final String id,
                        @RequestBody @Valid final AtualizacaoClientePJDTO atualizacaoClientePJDTO);

        @Operation(summary = "Crie um novo cliente PJ", method = "POST")
        @ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Cliente PJ criado com sucesso"),
                        @ApiResponse(responseCode = "400", description = "Corpo da requisicao invalido", content = @Content(schema = @Schema(oneOf = {
                                        CorpoRequisicaoInvalidoDTO.class, ExcecaoDTO.class }), examples = {
                                                        @ExampleObject(name = "Corpo da requisicao invalido #1", value = "{\"mensagem\": \"Corpo da requisicao invalido\", \"codigoStatus\": 400, \"erros\": {\"cnpj\": \"nao deve ser nulo\"}}"),
                                                        @ExampleObject(name = "Corpo da requisicao invalido #2", value = "{\"mensagem\": \"JSON parse error: Cannot deserialize value of type `java.lang.Integer` from String \\\"abc\\\": not a valid `java.lang.Integer` value\", \"codigoStatus\": 400}") })),
                        @ApiResponse(responseCode = "409", description = "Ja existe um cliente PJ para os dados fornecidos", content = @Content(schema = @Schema(implementation = ExcecaoDTO.class), examples = {
                                        @ExampleObject(name = "", value = "{\"mensagem\": \"Ja existe um cliente PJ para os dados fornecidos\", \"codigoStatus\": 409}") })),
                        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(schema = @Schema(implementation = ExcecaoDTO.class), examples = {
                                        @ExampleObject(name = "", value = "{\"mensagem\": \"Erro interno do servidor\", \"codigoStatus\": 500}") })) })
        @PostMapping(consumes = "application/json")
        public ResponseEntity<RespostaCriacaoClientePJDTO> criar(
                        @RequestBody @Valid final CriacaoClientePJDTO criacaoClientePJDTO);
}
