package com.challenge1.challenge1.repositorios;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.challenge1.challenge1.dominio.cliente_pf.ClientePF;

public interface RepositorioClientePF extends JpaRepository<ClientePF, UUID> {
}
