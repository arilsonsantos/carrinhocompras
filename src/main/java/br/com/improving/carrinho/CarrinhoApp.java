package br.com.improving.carrinho;

import br.com.improving.carrinho.service.CompraService;
import br.com.improving.carrinho.service.ICompra;

public class CarrinhoApp {

    public static void main(String[] args) {
        ICompra compra = new CompraService();
        compra.executar();
    }

}