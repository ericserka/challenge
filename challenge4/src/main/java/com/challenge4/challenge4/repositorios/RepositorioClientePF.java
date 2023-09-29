package com.challenge4.challenge4.repositorios;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.challenge4.challenge4.dominio.cliente_pf.ClientePF;

public interface RepositorioClientePF extends JpaRepository<ClientePF, UUID> {
}
