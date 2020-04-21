package br.com.improving.carrinho.utils;

import java.util.List;
import java.util.Scanner;

import br.com.improving.carrinho.MensagemEnum;
import br.com.improving.carrinho.Produto;

public final class CompraUtils {

    public static Scanner prompt() {
        return new Scanner(System.in);
    }

    public static void listaProdutos(final List<Produto> produtos) {
        produtos.forEach(System.out::println);
    }

    public static void linhaTracejada() {
        System.out.println("---------------------------------------------------------------------");
    }

    public static void linhaEmBranco() {
        System.out.println();
    }

    public static void imprimirMensagem(final String mensagem) {
        System.out.println(mensagem);
    }

    public static void imprimirMensagem(final MensagemEnum mensagemEnum) {
        System.out.println(mensagemEnum.getDescricao());
    }

    public static void imprimirMensagem(final MensagemEnum mensagemEnum, String complemento) {
        System.out.println(mensagemEnum.getDescricao() + complemento);
    }

}