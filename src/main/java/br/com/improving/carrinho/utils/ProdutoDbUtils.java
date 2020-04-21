package br.com.improving.carrinho.utils;

import java.util.Arrays;
import java.util.List;

import br.com.improving.carrinho.Produto;

/**
 * Classe utils para simular um banco de dados.
 */
public final class ProdutoDbUtils {

    /**
     * Método que retorna uma lista de produtos previamente cadastrados para simulação.
     * 
     * @return List<Produto>
     */
    public static final List<Produto> findAll() {
        return Arrays.asList(new Produto(1L, "produto 01"), new Produto(2L, "produto 02"),
                new Produto(3L, "produto 03"));
    }

}