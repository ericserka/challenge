package com.challenge2.challenge2.repositorios;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.challenge2.challenge2.dominio.cliente_pj.ClientePJ;

public interface RepositorioClientePJ extends JpaRepository<ClientePJ, UUID> {
}
