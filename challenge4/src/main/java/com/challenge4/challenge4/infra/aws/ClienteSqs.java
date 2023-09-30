package com.challenge4.challenge4.infra.aws;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.challenge4.challenge4.dominio.cliente.Cliente;
import com.challenge4.challenge4.excecoes.FilaVaziaExcecao;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Component
@Slf4j
public class ClienteSqs {
    private SqsClient sqsClient;
    private ObjectMapper objectMapper;

    @Value("${aws.sqs.queue-url}")
    private String urlFila;

    @Value("${aws.accessKeyId}")
    private String accessKeyId;

    @Value("${aws.secretKey}")
    private String secretKey;

    @PostConstruct
    public void postConstructClienteSqs() {
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        this.sqsClient = SqsClient.builder()
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKeyId, secretKey)))
                .region(Region.US_EAST_1).build();
    }

    public void sendMessage(final Cliente cliente) {
        final SendMessageRequest requisicao = SendMessageRequest.builder()
                .queueUrl(this.urlFila)
                .messageGroupId("MyQueue.fifo")
                .messageBody(this.createMessage(cliente))
                .build();

        sqsClient.sendMessage(requisicao);
    }

    public Message receiveMessage() {
        final ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
                .queueUrl(this.urlFila)
                .maxNumberOfMessages(1)
                .build();

        final List<Message> messages = this.sqsClient.receiveMessage(receiveMessageRequest).messages();

        log.info("Mensagens recebidas: {}", messages);

        if (messages.isEmpty())
            throw new FilaVaziaExcecao();

        return messages.get(0);
    }

    public void deleteMessage(final Message message) {
        final DeleteMessageRequest deleteMessageRequest = DeleteMessageRequest.builder()
                .queueUrl(this.urlFila)
                .receiptHandle(message.receiptHandle())
                .build();

        this.sqsClient.deleteMessage(deleteMessageRequest);
    }

    private String createMessage(final Cliente cliente) {
        try {
            return this.objectMapper.writeValueAsString(cliente);
        } catch (JsonProcessingException e) {
            log.error("Erro ao serializar mensagem da fila: {} - {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "falha ao serializar mensagem da fila");
        }
    }
}
