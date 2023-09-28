package com.challenge1.challenge1.dominio.cliente_pf;

import com.challenge1.challenge1.dominio.EntidadeBase;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "clientes_pf")
@Table(name = "clientes_pf")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClientePF extends EntidadeBase {
    @Column(unique = true)
    private String cpf;

    private Integer mcc;

    private String nome;

    @Column(unique = true)
    private String email;
}
