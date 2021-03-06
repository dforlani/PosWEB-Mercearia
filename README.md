# Avaliação 1 - Lista de Exercícios

ET1) Descreva com suas palavras o funcionamento dos protocolos HTTP e HTTPS. Explique quais são as principais diferenças entre os dois protocolos.
O protocolo HTTP (Hyper Text Transport Protocol), permite a comunicação entre duas máquinas por meio da transferência de hiper-texto. Esta comunicação, norlmente, utiliza a porta 80 das máquinas. O protocolo funciona numa forma de requisição-resposta. De forma que o cliente inicia uma sessão HTTP enviando uma requisição para estabelecer uma conexão Transmission Control Protocol (TCP) com o servidor. O servidor que, previamente está ouvindo a porta 80, recebe a mensagem do cliente e responde, normalmente, com a informação solicitada.
Já o HTTPS (Hyper Text Transport Protocol Secure) é uma evolução do HTTP, para prover segurança na comunicação entre o cliente e o servidor, implantando mensagens criptografadas. Para isto, ele adiciona uma camada a mais na comunicação com o SSL (Secure Sockets Layer), o qual fica entre a camada de aplicação e a camada de transporte para prover a autenticação e encriptação dos pacotes.

ET2) Descreva com suas palavras o que é Representational State Transfer (REST). 
O REST (Representational State Transfer) foi criado por Roy Fielding em 2000 e é uma arquitetura web que fornece padrões para a comnicação entre sistemas de computador na web. Sistemas que adotam essa arquitetura são chamados de RESTful e adotam os seguintes conceitos:
- Client-server: a implementação do cliente e do servidor são feitas de forma independente. Desta forma, cada parte conhece antecipadamente o formato das mensagens a serem utilizados e podem evoluir independemente.
- Stateless: ser "sem estado" significa que cliente e servidor não precisam saber o atual estado em que a outro se encontra. Desta forma, as mensagens precisam ser completas o suficiente para que o recebedor entenda a requisição.
- Cacheable: as respostas as requisições precisam atender as restrições de cache, de forma que indiquem se as informações nela contidas, são ou não cacheáveis.
- Uniform interface: esse conceito indica que a comunicação entre cliente e servidor precisa seguir uma interface uniforme, de forma a existir um padrão de comunicação entre as parte bem definido.
- Layered System: é possível adicionar camadas de sistemas para melhorar o desempenho. De forma que cada camada apenas conhece as camadas com as quais interage.

ET3) Escolha um dos ataques catalogados pela OWASP Foundation (https://owasp.org/www-community/attacks/). Pesquise sobre o ataque escolhido e sobre quais medidas devem ser tomadas para proteger um sistema de tal ataque.
Code Injection. Neste tipo, o ataque consiste na injeção de código malicioso que será interpretado e executado pelo servidor. Este difere do ataque Command Injection, por se limitar à linguagem que está sendo utilizada no sistema. Desta forma, um código PHP pode ser injetado e executado em um servidor que esteja utilizando na aplicação web. Para tentar evitá-lo, deve-se:
- não executar comandos em dados não confiáveis;
- sempre validar os dados que são entrados;
- limitar ao mínimo as funcionalidade que o interpretador pode executar;
- executar análises estáticas para identificar vulnerabilidades; e
- executar regularmente avaliação de vulnerabilidades e testes de penetração.

Exercícios Práticos

EP1) Crie uma aplicação Spring Boot que seja capaz de atender requisições HTTP, do tipo GET, no caminho “/contador”. A resposta para este caminho deve ser uma página HTML contendo a quantidade de vezes que o caminho foi acessado (i.e., “Número de Acessos: XX”). O contador não precisa ser persistido, basta que seja armazenado em memória.

EP2) Considerando as entidades Pessoa, Produto, Pedido e PedidoItem apresentadas abaixo, realiza a implementação de uma aplicação Spring Boot, contendo as seguintes funcionalidades: (1) operações CRUD para Pessoa e Produto, e (2) movimento de realizar pedido. Deve-se realizar a persistência de dados com Spring Data. Além disso, deve-se prover acesso às funcionalidades do sistema por meio de uma interface web (e.g., usando o Thymeleaf). O valor do produto está em Float, mas pode-se usar outra forma mais adequada para valores monetários, tal como o uso de BigDecimal.
