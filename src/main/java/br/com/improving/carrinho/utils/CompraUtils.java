package br.com.improving.carrinho.utils;

import java.util.List;
import java.util.Scanner;

import br.com.improving.carrinho.Produto;
import br.com.improving.enumerates.MensagemEnum;
import br.com.improving.enumerates.MensagemErroEnum;

/**
 * Classe utils para fornecer alguns métodos de leitura e exibição de mensagens.
 */
public final class CompraUtils {

    /**
     * Retorna uma instancia da classe Scanner.
     * 
     * @return new Scanner(System.in)
     */
    public static Scanner prompt() {
        return new Scanner(System.in);
    }

    /**
     * Imprime a lista de produtos. 
     * 
     * @param produtos
     */
    public static void listaProdutos(final List<Produto> produtos) {
        produtos.forEach(System.out::println);
    }

    /**
     * Imprime uma sequencia do character "-".
     */
    public static void linhaTracejada() {
        System.out.println("---------------------------------------------------------------------");
    }

    /**
     * Imprime uma linha em branco.
     */
    public static void linhaEmBranco() {
        System.out.println();
    }

    /**
     * Imprime uma mensagem recebida por parâmetro.
     * 
     * @param mensagem
     */
    public static void imprimirMensagem(final String mensagem) {
        System.out.println(mensagem);
    }

    /**
     * Imprime a descricao do enum passado como parametro
     * 
     * @param mensagemEnum
     */
    public static void imprimirMensagem(final MensagemEnum mensagemEnum) {
        System.out.println(mensagemEnum.getDescricao());
    }

    /**
     * Imprime a descrição e um complemento da mensagem, ambos passados por parâmetro
     * 
     * @param mensagemEnum
     * @param complemento
     */
    public static void imprimirMensagem(final MensagemEnum mensagemEnum, String complemento) {
        System.out.println(mensagemEnum.getDescricao() + complemento);
    }

    /**
     * Imprime a descricao do enum com mensagem de erro passado como parametro
     * 
     * @param mensagemErroEnum
     */
    public static void imprimirMensagem(final MensagemErroEnum mensagemErroEnum) {
        System.out.println(mensagemErroEnum.getDescricao());
    }

}