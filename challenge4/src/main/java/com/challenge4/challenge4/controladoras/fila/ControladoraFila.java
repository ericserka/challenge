package com.challenge4.challenge4.controladoras.fila;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import com.challenge4.challenge4.dominio.cliente.Cliente;
import com.challenge4.challenge4.excecoes.ExcecaoDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Fila")
public interface ControladoraFila {
    @Operation(summary = "Retira (desenfileira) o próximo cliente da fila", method = "GET")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Cliente desenfileirado com sucesso"),
            @ApiResponse(responseCode = "204", description = "Fila vazia", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(schema = @Schema(implementation = ExcecaoDTO.class), examples = {
                    @ExampleObject(name = "", value = "{\"mensagem\": \"Erro interno do servidor\", \"codigoStatus\": 500}") })) })
    @GetMapping("/desenfileirar")
    public ResponseEntity<Cliente> desenfileirarCliente();
}
