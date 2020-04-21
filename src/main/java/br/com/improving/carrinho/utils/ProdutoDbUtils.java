package br.com.improving.carrinho.utils;

import java.util.Arrays;
import java.util.List;

import br.com.improving.carrinho.Produto;

public final class ProdutoDbUtils {

    public static final List<Produto> findAll() {
        return Arrays.asList(new Produto(1L, "produto 01"), new Produto(2L, "produto 02"),
                new Produto(3L, "produto 03"));
    }

}