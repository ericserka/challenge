package com.challenge2.challenge2.dominio;

import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
public abstract class EntidadeBase {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Setter
    protected UUID id;

    @CreationTimestamp
    protected Instant criadoEm;

    @UpdateTimestamp
    protected Instant atualizadoEm;
}
