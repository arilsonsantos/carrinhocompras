Nesta prova será necessário implementar os métodos das classes conforme descrições dos Javadocs correpondentes.

Você poderá criar novos atributos e métodos, mas é proibido mover as classes de pacote. Também é proibido mudar a assinatura dos métodos e construtores já existentes, exceto o construtor da classe CarrinhoCompras.

A prova deve ser resolvida utilizando obrigatoriamente Java 8. Não será necessário implementar nenhum tipo de interface gráfica ou mecanismo de persistência.

Todos os requisitos funcionais tem que ser atendidos 100% para que sua solução seja aprovada. Sua prova precisa compilar via Maven, impreterivelmente - não basta rodar apenas via sua IDE.

---
### Instruções para a execução da aplicação Carrinho de Compras.

##### 1) No diretório da aplicação, digite a instrução abaixo e siga as instruções:

 <kbd>mvn package exec:java</kbd> 


#### Fluxograma:


![Logo](./documentacao/carrinho-de-compras-retrato.png)

### Exemplos
Inicia uma compra
Adiciona o produto 1 ao carrinho
Adiciona novamente o produto 1, com preço inválido (exceção lançada)
Adiciona novamente o produto 1, com o preço correto
Adiciona o produto 2 ao carrinho
Fecha o carrinho
Sai da aplicação
![v-01](./documentacao/01-compra-1-carrinho.gif)


Inicia uma compra (sessão de 5 segundos)
Adiciona o produto 1 ao carrinho
Sessão expira
Carrinho descartado
Novo carrinho é adicionado
Adiciona 02 itens ao carrinho
Sessao expira novamente
Sai da aplicação
![v-01](./documentacao/02-sessao-expirando.gif)


Inicia com valor inválido par a sessão, o valor 30 é assumido
Inicia uma compra
Adiciona 02 itens ao carrinho de compras
Fecha carrinho
Inicia nova compra
Adiciona 01 item ao carrinho de compras
Fecha o carrinho
Inicia nova compra
Adiciona 01 item ao carrinho de compras
Cancela a compra
Sai da aplicação
![v-01](./documentacao/03-adiciona-3-carrinhos-cancela-1.gif)
