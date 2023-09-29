package com.challenge3.challenge3.repositorios;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.challenge3.challenge3.dominio.cliente_pj.ClientePJ;

public interface RepositorioClientePJ extends JpaRepository<ClientePJ, UUID> {
}
