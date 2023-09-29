package com.challenge4.challenge4.dtos.cliente_pj;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AtualizacaoClientePJDTO(
                @NotNull @Min(00000000000L) @Max(99999999999999L) Long cnpj,
                @NotNull @NotBlank @NotEmpty @Size(max = 50) String razaoSocial,
                @NotNull @Min(0000) @Max(9999) Integer mcc,
                @NotNull @Min(00000000000L) @Max(99999999999L) Long cpfContato,
                @NotNull @NotBlank @NotEmpty @Size(max = 50) String nomeContato,
                @NotNull @NotBlank @NotEmpty @Pattern(regexp = "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$") String emailContato) {
}
