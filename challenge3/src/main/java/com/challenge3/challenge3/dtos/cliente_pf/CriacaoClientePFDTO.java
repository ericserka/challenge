package com.challenge3.challenge3.dtos.cliente_pf;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CriacaoClientePFDTO(
                @NotNull @Min(0000) @Max(9999) Integer mcc,
                @NotNull @Min(00000000000L) @Max(99999999999L) Long cpf,
                @NotNull @NotBlank @NotEmpty @Size(max = 50) String nome,
                @NotNull @NotBlank @NotEmpty @Pattern(regexp = "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$") String email) {
}
