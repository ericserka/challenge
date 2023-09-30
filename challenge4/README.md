# Desafio 4

## Informações básicas

- Como a fila deveria ser a mesma independente do tipo de cliente (pessoa física ou jurídica), foi criada uma classe `Cliente` que consegue conter os dados tanto do `ClientePF` quanto do `ClientePJ` e todos os elementos da fila são do tipo `Cliente`.
- Foi utilizado o banco de dados em memória H2 para a persistência dos dados, o que significa que todos dados serão perdidos ao encerrar a aplicação.
- Para acessar o swagger, basta compilar a aplicação e abrir a url [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html) no seu navegador. É possível testar os endpoints por lá, além de ter documentado as entradas e saídas possíveis.

## Débito técnico

**a) Identifique um débito técnico de Segurança da Informação na aplicação**

As propriedades básicas da segurança da informação são: confidencialidade, integridade, disponibilidade, autenticidade e legalidade. A propriedade identificada nesse desafio como débito técnico é a de integridade da informação.

**b) Detalhe o débito técnico identificado, informando a criticidade e possíveis consequências**

As razões da aplicação desenvolvida até o momento não garantir a integridade dos dados/informação são basicamente duas:

1. Quando um usuário solicita a exclusão de um cliente, esse cliente deveria ser removido da fila de atendimento (em caso de estar presente). Isso não acontece, o que levaria ao usuário atender um cliente não mais existente.
2. Quando um cliente é atualizado, deveria ser checado se esse cliente já está presente em alguma posição da fila. Em caso positivo, essa versão antiga do cliente deveria ser removida da fila em favor da inclusão da nova versão com os dados atualizados. Isso não acontece, o que levaria ao usuário atender um cliente com dados desatualizados.

Apesar de ser possível listar mais de um elemento da fila na solução SQS da AWS, a quantidade máxima de elementos retornados é de 10, conforme mostra a documentação do SDK AWS para Java abaixo:

![image](https://github.com/ericserka/challenge/assets/45241755/68bf63a8-113e-4022-a448-2c62bf07b2c5)

Dessa forma, se a fila de atendimento possuir 20 clientes, eu só vou conseguir analisar os últimos 10. Portanto a solução SQS da AWS não resolve o problema identificado aqui.

No próximo item, será visto em detalhes como uma nova solução poderia ser desenvolvida.

**c) Planeje as atividades técnicas para o desenvolvimento da solução**

Como foi visto no item anterior, é preciso ter acesso fácil a todos os elementos presentes na fila de atendimento. Ter acesso apenas aos 10 últimos não é o suficiente, já que 100% dos dados/informações não estariam sendo verificados.

Como solução para esse problema, existe o [Redis sorted sets](https://redis.io/docs/data-types/sorted-sets/), que resumidamente é uma coleção de dados ordenados por uma pontuação associada. Com essa pontuação eu consigo respeitar a ordem FIFO (First In, First Out) e simular uma estrutura de dados de fila, apesar do [Redis](https://redis.io/) ser um banco de dados NoSQL de chave e valor. Para isso, seria um sorted set com pontuação associado ao tempo de inserção na coleção. De tal forma que o menor tempo signifique uma maior pontuação. Ou seja, a pontuação é inversamente proporcional ao tempo de inserção. Por exemplo, o UNIX Timestamp 1696034814 teria uma pontuação maior que o UNIX Timestamp 1696034875, pois o primeiro aconteceu primeiro no tempo quando comparado com o segundo.

A AWS tem um serviço chamado Amazon MemoryDB for Redis, que ela mesma o define da seguinte forma: "Serviço de banco de dados na memória, durável e compatível com Redis para uma performance ultrarrápida". Com esse serviço, seria possível conectar a aplicação a um banco de dados Redis na nuvem e utilizar as funcionalidades do Redis sorted sets.

Para fazer a conexão com o MemoryDB da AWS na aplicação Java Spring Boot, seria necessário utilizar uma dependência que é o client de redis para o Java, chamada [Jedis](https://github.com/redis/jedis).

Ter a ordem dos elementos preservada é o primeiro passo. E isso é possível com o Redis sorted sets. Agora basta utilizar a robustez e facilidade do Redis para recuperar e remover clientes da coleção. Para os dois casos problemáticos detalhados no item b, a solução seria a seguinte:

1. Utilizar o comando `ZRANGE myzset 0 -1` para obter todos os elementos do set (coleção) ordenados pela pontuação de maneira ascendente. O comando `ZRANGE` retornaria um array (com ou sem elementos). A partir daí eu teria que fazer uma busca pelo ID do cliente que foi removido. Se um elemento fosse encontrado, executar o comando `ZREM myzset "<ID_DO_CLIENTE>"`. Caso contrário, não seria necessário fazer nada pois o cliente a ser removido não estava presente na fila de atendimento.
2. Para o caso de atualização de um cliente, inicialmente seguimos os exatos mesmos passos do caso de remoção: listo todos, procuro se existe um elemento que possua ID igual ao ID do cliente e, caso exista, removo o elemento. Feito isso, deve-se adicionar o novo elemento atualizado na coleção. Para isso, basta executar o comando `ZADD myzset <UNIX_TIMESTAMP_ATUAL> <ID_DO_CLIENTE>`.

Observações:

- myzset é o nome arbitrário para a coleção. Ele poderia ser algo como "filaAtendimento" sem problemas.
- Para gerar o UNIX_TIMESTAMP_ATUAL em java, basta utilizar o Instant, que vem do package java.time, da seguinte forma: `Instant.now().toEpochMilli()`
- Para o caso de criação de novos clientes, basta executar o comando `ZADD myzset <UNIX_TIMESTAMP_ATUAL> <ID_DO_CLIENTE>`. Da mesma maneira que é feito no caso de atualização de cliente, só que sem a necessidade de fazer a remoção antes (já que o cliente acabou de ser criado).

**d) Implemente a solução**

