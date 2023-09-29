# Desafio 3

- Como a fila deveria ser a mesma independente do tipo de cliente (pessoa física ou jurídica), foi criada uma classe `Cliente` que consegue conter os dados tanto do `ClientePF` quanto do `ClientePJ` e todos os elementos da fila são do tipo `Cliente`.
- Foi utilizado o banco de dados em memória H2 para a persistência dos dados, o que significa que todos dados serão perdidos ao encerrar a aplicação.
- Para acessar o swagger, basta compilar a aplicação e abrir a url [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html) no seu navegador. É possível testar os endpoints por lá, além de ter documentado as entradas e saídas possíveis.