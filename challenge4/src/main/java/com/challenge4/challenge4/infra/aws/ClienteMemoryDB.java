package com.challenge4.challenge4.infra.aws;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.challenge4.challenge4.dominio.cliente.Cliente;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.JedisPooled;

@Component
@Slf4j
public class ClienteMemoryDB {
    @Value("${aws.memorydb.host}")
    private String hostName;

    @Value("${aws.memorydb.port}")
    private int port;

    @Value("${aws.memorydb.clientName}")
    private String clientName;

    @Value("${aws.memorydb.userName}")
    private String userName;

    @Value("${aws.memorydb.password}")
    private String password;

    private JedisPooled jedisCluster;
    private ObjectMapper objectMapper;

    @PostConstruct
    public void postContructorClienteMemoryDB() {
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        this.jedisCluster = new JedisPooled(hostName, port);
    }

    public Cliente getCliente(final String id) {
        final List<String> result = this.jedisCluster.zrange(id, 0, -1);
        if (result.isEmpty()) {
            throw new RuntimeException("Cliente não encontrado");
        }
        final String clienteJson = result.get(0);
        try {
            return objectMapper.readValue(clienteJson, Cliente.class);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao converter cliente");
        }
    }

    public void upsertCliente(final String id, final Cliente cliente) {
        final long score = Instant.now().toEpochMilli();
        final long result = jedisCluster.zadd(id, score, cliente.toString());
        log.info("cliente {} - {}", id, result == 1L ? "inserido" : "atualizado");
    }

    public void deleteCliente(final String id) {
        final long delCount = jedisCluster.zrem(id);
        if (delCount != 1L) {
            throw new RuntimeException("Erro ao deletar cliente");
        }
    }

}