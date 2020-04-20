package br.com.improving.carrinho;

import java.util.Arrays;
import java.util.List;

public final class ProdutoDbUtils {

    public static final List<Produto> findAll() {
        return Arrays.asList(new Produto(1L, "produto 01"), new Produto(2L, "produto 02"),
                new Produto(3L, "produto 03"));
    }

}