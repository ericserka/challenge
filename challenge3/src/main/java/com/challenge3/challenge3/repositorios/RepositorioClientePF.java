package com.challenge3.challenge3.repositorios;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.challenge3.challenge3.dominio.cliente_pf.ClientePF;

public interface RepositorioClientePF extends JpaRepository<ClientePF, UUID> {
}
