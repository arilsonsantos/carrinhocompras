package br.com.improving.carrinho;

import static br.com.improving.carrinho.MensagemEnum.CONFIRMAR_CANCELAR_COMPRA;
import static br.com.improving.carrinho.MensagemEnum.DESEJA_CONTINUAR_COMPRANDO;
import static br.com.improving.carrinho.MensagemEnum.DIGITE_A_QUANTIDADE;
import static br.com.improving.carrinho.MensagemEnum.DIGITE_CODIGO_DO_PRODUTO;
import static br.com.improving.carrinho.MensagemEnum.DIGITE_NOME_CLIENTE;
import static br.com.improving.carrinho.MensagemEnum.DIGITE_O_PRECO;
import static br.com.improving.carrinho.MensagemEnum.MEDIA_DO_VALOR_DOS_CARRINHOS;
import static br.com.improving.carrinho.MensagemEnum.NAO_HA_CARRINHO_COM_COMPRAS;
import static br.com.improving.carrinho.MensagemEnum.PARA_CANCELAR_2;
import static br.com.improving.carrinho.MensagemEnum.PARA_CONFIRMAR_1;
import static br.com.improving.carrinho.MensagemEnum.RESUMO_CARRINHO_CLIENTE;
import static br.com.improving.carrinho.MensagemEnum.RESUMO_DOS_CARRINHOS;
import static br.com.improving.carrinho.MensagemEnum.SAIR_DA_APLICACAO;
import static br.com.improving.carrinho.MensagemEnum.SESSAO_EXPIROU;
import static br.com.improving.carrinho.MensagemEnum.TOTAL_DO_PEDIDO;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;

public class Aplicacao {
    private static final List<Produto> produtos = ProdutoDbUtils.findAll();

    private static Long codigoProduto = null;
    private static String cliente = "";
    private static int sessao = 30;

    public static void main(final String[] args) {
        final CarrinhoComprasFactory factory = new CarrinhoComprasFactory();
        final Scanner ler = new Scanner(System.in);

        imprimirMensagem("Digite o tempo, em segundos, da sessão ou digite zero para o manter o valor igual a 30.");
        
        sessao = ler.nextInt();

        iniciarCompra(factory, ler);
        cancelarOuFinalizarCompra(factory, ler);
        isSairDaAplicacao(factory, ler);

        ler.close();
    }

    private static void iniciarCompra(final CarrinhoComprasFactory factory, final Scanner ler) {
        linhaEmBranco();
        imprimirMensagem(DIGITE_NOME_CLIENTE);

        cliente = ler.next();
        factory.criar(cliente);

        adicionaItemAoCarrinho(factory, ler, cliente);
        linhaEmBranco();
    }

    private static void cancelarOuFinalizarCompra(final CarrinhoComprasFactory factory, final Scanner ler) {
        if (factory.criar(cliente).getItens().isEmpty()){
            factory.invalidar(cliente);
            return;
        }

        final LocalTime inicioSessao = LocalTime.now();
        linhaEmBranco();
        imprimirMensagem(CONFIRMAR_CANCELAR_COMPRA);
        linhaEmBranco();

        imprimirMensagem(PARA_CONFIRMAR_1);
        imprimirMensagem(PARA_CANCELAR_2);

        final int cancelarCarrinho = ler.nextInt();
        final boolean sessaoExpirou = isSessaoExpirou(inicioSessao, factory, cliente, ler);

        if (cancelarCarrinho == 2 || sessaoExpirou) {
            isCancelarCarrinho(factory, cancelarCarrinho, ler);
        }
    }

    private static void isCancelarCarrinho(final CarrinhoComprasFactory factory, final int cancelarCarrinho,
            final Scanner ler) {
        if (cancelarCarrinho == 2) {
            factory.invalidar(cliente);
            System.out.println();
        }
    }

    private static void resumoDosCarrinhos(final CarrinhoComprasFactory factory) {
        linhaEmBranco();
        linhaTracejada();
        imprimirMensagem(RESUMO_DOS_CARRINHOS);
        linhaTracejada();
        linhaEmBranco();

        factory.clienteCarrinho.forEach((k, v) -> {
            System.out.println(k);
            v.getItens().forEach(System.out::println);
            imprimirMensagem(TOTAL_DO_PEDIDO, " " + v.getValorTotal() + "\n");
        });

        if (factory.clienteCarrinho.isEmpty()) {
            imprimirMensagem(NAO_HA_CARRINHO_COM_COMPRAS);
        } else {
            linhaEmBranco();
            imprimirMensagem(MEDIA_DO_VALOR_DOS_CARRINHOS, factory.getValorTicketMedio().toString());
            linhaEmBranco();
        }

        linhaTracejada();
    }

    private static void adicionaItemAoCarrinho(final CarrinhoComprasFactory factory, final Scanner ler,
            final String cliente) {

        linhaEmBranco();
        listaProdutos(produtos);
        linhaEmBranco();

        imprimirMensagem(DIGITE_CODIGO_DO_PRODUTO);
        codigoProduto = ler.nextLong();

        final Produto produto = produtos.stream().filter(p -> p.getCodigo().equals(codigoProduto)).findFirst()
                .orElse(null);

        imprimirMensagem(DIGITE_O_PRECO);
        BigDecimal valorUnitario = ler.nextBigDecimal();

        imprimirMensagem(DIGITE_A_QUANTIDADE);
        int quantidade = ler.nextInt();

        try {
            linhaEmBranco();
            factory.criar(cliente).adicionarItem(produto, valorUnitario, quantidade);
        } catch (ItemException ex) {
            imprimirMensagem(ex.getMessage());
        }
        linhaEmBranco();

        continuarCompra(factory, ler, cliente);
    }

    private static void continuarCompra(final CarrinhoComprasFactory factory, final Scanner ler, final String cliente) {
        imprimirMensagem(DESEJA_CONTINUAR_COMPRANDO);
        LocalTime inicioSessao = LocalTime.now();
        String adicionaMais = ler.next();

        boolean isSessaoExpirou = isSessaoExpirou(inicioSessao, factory, cliente, ler);

        if (isSessaoExpirou) {
            if (adicionaMais.equalsIgnoreCase("N")) {
                imprimirMensagem(RESUMO_CARRINHO_CLIENTE, cliente);
                linhaTracejada();

                if (!factory.criar(cliente).getItens().isEmpty()) {
                    factory.criar(cliente).getItens().forEach(System.out::println);
                    imprimirMensagem(TOTAL_DO_PEDIDO, factory.criar(cliente).getValorTotal().toString());
                    linhaEmBranco();
                }
            }
        } else {
            if (adicionaMais.equalsIgnoreCase("S")) {
                adicionaItemAoCarrinho(factory, ler, cliente);
            }
        }
    }

    private static boolean isSessaoExpirou(LocalTime inicioSessao, final CarrinhoComprasFactory factory,
            final String cliente, final Scanner ler) {
        if (Duration.between(inicioSessao, LocalTime.now()).getSeconds() > sessao) {
            if (factory.clienteCarrinho.isEmpty()) {
                factory.invalidar(cliente);
            }

            imprimirMensagem(SESSAO_EXPIROU);
            linhaEmBranco();

            factory.invalidar(cliente);
            isSairDaAplicacao(factory, ler);

            return true;
        }

        inicioSessao = LocalTime.now();
        return false;
    }

    private static void isSairDaAplicacao(final CarrinhoComprasFactory factory, final Scanner ler) {
        linhaEmBranco();
        imprimirMensagem(SAIR_DA_APLICACAO);

        final String sair = ler.next();
        
        if (sair.equalsIgnoreCase("N")) {
            iniciarCompra(factory, ler);
            isSairDaAplicacao(factory, ler);
        } else {
            resumoDosCarrinhos(factory);
            System.exit(0);
        }
    }

    private static void listaProdutos(final List<Produto> produtos) {
        produtos.forEach(System.out::println);
    }

    private static void linhaTracejada() {
        System.out.println("---------------------------------------------------------------");
    }

    private static void linhaEmBranco() {
        System.out.println();
    }

    private static void imprimirMensagem(final String mensagem) {
        System.out.println(mensagem);
    }
    private static void imprimirMensagem(final MensagemEnum mensagemEnum) {
        System.out.println(mensagemEnum.getDescricao());
    }

    private static void imprimirMensagem(final MensagemEnum mensagemEnum, String complemento) {
        System.out.println(mensagemEnum.getDescricao() + complemento);
    }



}