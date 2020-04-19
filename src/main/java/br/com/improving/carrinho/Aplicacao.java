package br.com.improving.carrinho;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Aplicacao {

    static Long codigoProduto = null;

    public static void main(String[] args) {

        CarrinhoComprasFactory factory = new CarrinhoComprasFactory();
        boolean adicionaMaisCarrinhos = true;
        boolean invalidarCarrinho = true;

        //SIMULAÇÃO DO BANCO DE DADOS DE PRODUTOS
        List<Produto> produtos = Arrays.asList(new Produto(1L, "produto 01"), new Produto(2L, "produto 02"),
                new Produto(3L, "produto 03"));

        //INÍCIO DA INTERAÇAO
        Scanner ler = new Scanner(System.in);

        String cliente = "";
        while (adicionaMaisCarrinhos) {
            System.out.println("Digite o nome do cliente:");
            cliente = ler.next();
            factory.criar(cliente);

            boolean adicionaMaisItems = true;
            while (adicionaMaisItems) {
                adicionaMaisItems = adicionaItemAoCarrinho(factory, produtos, ler, cliente);
            }

            isConsultarCarrinho(factory, ler);

            System.out.println("\nDeseja criar um novo carrinho? [S/N]:");

            String adicionarCarrinho = ler.next();
            linhaEmBranco();
            adicionaMaisCarrinhos = adicionarCarrinho.equalsIgnoreCase("S") ? true : false;
        }

        isInvalidarCarrinho(factory, cliente, ler, invalidarCarrinho);

        resumoDosCarrinhos(factory);

        ler.close();

    }

    private static void isInvalidarCarrinho(CarrinhoComprasFactory factory, String cliente, Scanner ler, boolean primeiraPergunta) {
        if (primeiraPergunta){
            System.out.println("\nDeseja cancelar uma carrinho? [S/N]:");
        }
        String cancelarCarrinho = ler.next();

        isCancelarCarrinho(factory, cliente, cancelarCarrinho, ler, primeiraPergunta);

        System.out.println();
    }

    private static void isCancelarCarrinho(CarrinhoComprasFactory factory, String cliente, String cancelarCarrinho, Scanner ler, boolean primeiraPergunta) {
        primeiraPergunta=false;
        if (cancelarCarrinho.equalsIgnoreCase("S")) {
            System.out.println("\nDigite o nome do cliente:");
            String nomeCliente = ler.next();
            factory.invalidar(nomeCliente);
            System.out.println();
            System.out.println("\nDeseja cancelar outro carrinho? [S/N]:");

            isInvalidarCarrinho(factory, cliente, ler, primeiraPergunta);
        }
    }

    private static void resumoDosCarrinhos(CarrinhoComprasFactory factory) {
        imprimeLinha();
        System.out.println("RESUMO DOS CARRINHOS:");
        imprimeLinha();
        linhaEmBranco();

        factory.clienteCarrinho.forEach((k, v) -> {
            System.out.println(k);
            v.getItens().forEach(System.out::println);
            System.out.println("Total do pedido: " + v.getValorTotal() + "\n");
        });

        System.out.println("\nMédia do valor dos carrinhos: " + factory.getValorTicketMedio() + "\n");
        imprimeLinha();
    }

    private static void isConsultarCarrinho(CarrinhoComprasFactory factory, Scanner ler) {
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
    }

    private static boolean adicionaItemAoCarrinho(CarrinhoComprasFactory factory, List<Produto> produtos, Scanner ler,
            String cliente) {
        BigDecimal valorUnitario;
        int quantidade;

        linhaEmBranco();
        listaProdutos(produtos);

        System.out.println("\nDigite o código do produto:");
        codigoProduto = ler.nextLong();
        Produto produto = produtos.stream().filter(p -> p.getCodigo().equals(codigoProduto)).findFirst().orElse(null);

        System.out.println("Digite o preço:");
        valorUnitario = ler.nextBigDecimal();

        System.out.println("Digite a quantidade:");
        quantidade = ler.nextInt();

        factory.criar(cliente).adicionarItem(produto, valorUnitario, quantidade);

        System.out.println("\nDeseja comprar mais?: [S/N]");
        String adicionaMais = ler.next();
        return adicionaMais.equalsIgnoreCase("S") ? true : false;
    }

    private static void listaProdutos(List<Produto> produtos) {
        produtos.forEach(System.out::println);
    }

    private static void imprimeLinha() {
        System.out.println("---------------------------------------------------------------");
    }
    private static void linhaEmBranco(){
        System.out.println();
    }

}