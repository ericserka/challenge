package com.challenge4.challenge4.dominio.cliente_pj;

import com.challenge4.challenge4.dominio.EntidadeBase;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "clientes_pj")
@Table(name = "clientes_pj")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientePJ extends EntidadeBase {
    @Column(unique = true)
    private String cnpj;

    @Column(unique = true)
    private String razaoSocial;

    private Integer mcc;

    private String cpfContato;

    private String nomeContato;

    private String emailContato;
}
