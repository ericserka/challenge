package com.challenge1.challenge1.repositorios;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.challenge1.challenge1.dominio.cliente_pj.ClientePJ;

public interface RepositorioClientePJ extends JpaRepository<ClientePJ, UUID> {
}
