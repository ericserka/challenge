# Desafio 1

Apesar dos clientes PF (pessoa física) e PJ (pessoa jurídica) terem alguns dados em comum (CPF, email e MCC), foi decidido fazer a persistência dos dados em duas entidades diferentes: Cliente PF e Cliente PJ. 

A razão disso é que um mesmo email pode ser tanto Cliente PF quanto cliente PJ. Além disso, um email pode pertencer a vários Clientes PJ, o que também faz sentido numa aplicação real, já que empresas de mesmo dono podem ter o mesmo email para contato.

- Para acessar o swagger, basta compilar a aplicação e abrir a url [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html) no seu navegador.
- A aplicação contou com cobertura de 76% de testes, conforme mostra a figura `cobertura_testes.png` no diretório raíz do projeto:
- Para acessar o HTML detalhado a respeito da cobertura de testes, execute o comando `./mvnw test` no seu terminal e abra o arquivo `target/site/jacoco/index.html` no seu navegador.