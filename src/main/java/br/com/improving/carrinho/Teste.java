package br.com.improving.carrinho;

import java.math.BigDecimal;

public class Teste {
    public static void main(String[] args) {
        
        CarrinhoComprasFactory factory = new CarrinhoComprasFactory();

        CarrinhoCompras carrinho1 = factory.criar("Joao");
        carrinho1 = factory.criar("Joao");

        carrinho1.adicionarItem(new Produto(1L, "produto 01"), new BigDecimal(10.45), 10);
        carrinho1.adicionarItem(new Produto(1L, "produto 01"), new BigDecimal(10.33), 5);
        carrinho1.adicionarItem(new Produto(2L, "produto 02"), new BigDecimal(10.33), 5);

        carrinho1.getItens().forEach(i -> i.getProduto());
        carrinho1.getItens().forEach(System.out::println);

        System.out.println(factory.getValorTicketMedio());

    }

    
}