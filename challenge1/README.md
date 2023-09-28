# Desafio 1

Apesar dos clientes PF (pessoa física) e PJ (pessoa jurídica) terem alguns dados em comum (CPF, email e MCC), foi decidido fazer a persistência dos dados em duas entidades diferentes: Cliente PF e Cliente PJ. 

A razão disso é que um mesmo email pode ser tanto Cliente PF quanto cliente PJ. Além disso, um email pode pertencer a vários Clientes PJ, o que também faz sentido numa aplicação real, já que empresas de mesmo dono podem ter o mesmo email para contato.

- Para acessar o swagger, basta compilar a aplicação e abrir a url [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html) no seu navegador. É possível testar os endpoints por lá, além de ter documentado as entradas e saídas possíveis.
- A aplicação contou com cobertura de 90% de testes, conforme mostra a figura `cobertura_testes_1.jpg` no diretório raíz do projeto.