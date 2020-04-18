package br.com.improving.carrinho;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Aplicacao {

    static Long codigoProduto = null;

    public static void main(String[] args) {

        CarrinhoComprasFactory factory = new CarrinhoComprasFactory();

        //SIMULAÇÃO DO BANCO DE DADOS DE PRODUTOS
        List<Produto> produtos = Arrays.asList(new Produto(1L, "produto 01"), new Produto(2L, "produto 02"),
                new Produto(3L, "produto 03"));

        BigDecimal valorUnitario = BigDecimal.ZERO;
        int quantidade = 0;

        //INTERAÇAO
        Scanner ler = new Scanner(System.in);
        boolean adicionaMaisCarrinhos = true;

        while (adicionaMaisCarrinhos) {
            System.out.println("Digite o nome do cliente:");
            String cliente = ler.next();
            factory.criar(cliente);

            boolean adicionaMaisItems = true;
            while (adicionaMaisItems) {
                System.out.println();
                listaProdutos(produtos);

                System.out.println("\nDigite o código do produto:");
                codigoProduto = ler.nextLong();
                Produto produto = produtos.stream().filter(p -> p.getCodigo().equals(codigoProduto)).findFirst()
                        .orElse(null);

                System.out.println("Digite o preço:");
                valorUnitario = ler.nextBigDecimal();

                System.out.println("Digite a quantidade:");
                quantidade = ler.nextInt();

                factory.criar(cliente).adicionarItem(produto, valorUnitario, quantidade);

                System.out.println("\nDeseja comprar mais?: [S/N]");
                String adicionaMais = ler.next();
                adicionaMaisItems = adicionaMais.equalsIgnoreCase("S") ? true : false;
            }

            System.out.println("\nPara consultar carrinho do cliente, digite o nome do cliente ou S par sair:\n");
            String clienteConsulta = ler.next();

            if (!clienteConsulta.equalsIgnoreCase("S")) {
                CarrinhoCompras carrinhoConsulta = factory.criar(clienteConsulta);
                if (carrinhoConsulta != null && !carrinhoConsulta.getItens().isEmpty()) {
                    carrinhoConsulta.getItens().forEach(System.out::println);
                    System.out.println("VALOR DO PEDIDO: " + carrinhoConsulta.getValorTotal());
                } else {
                    System.out.println("Carrinho vazio");
                }
            }

            System.out.println("\nDeseja criar um novo carrinho? [S/N]:");

            String adicionarCarrinho = ler.next();
            System.out.println();
            adicionaMaisCarrinhos = adicionarCarrinho.equalsIgnoreCase("S") ? true : false;
        }
        
        System.out.println("RESUMO DOS CARRINHOS:");
        System.out.println("---------------------------------------------------------------\n");
        
        factory.clienteCarrinho.forEach((k, v) -> {
            System.out.println(k);
            v.getItens().forEach(System.out::println);
            System.out.println("Total do pedido: " + v.getValorTotal() + "\n");
        });
        
        System.out.println("\nMédia do valor dos carrinhos: " + factory.getValorTicketMedio() + "\n");
        System.out.println("---------------------------------------------------------------\n");

        ler.close();

    }

    private static void listaProdutos(List<Produto> produtos) {
        produtos.forEach(System.out::println);

    }

}