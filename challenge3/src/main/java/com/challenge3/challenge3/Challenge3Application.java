package com.challenge3.challenge3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Desafio 1 - Swagger", version = "1", description = "Desafio 1 API docs"))
public class Challenge3Application {

	public static void main(String[] args) {
		SpringApplication.run(Challenge3Application.class, args);
	}

}
