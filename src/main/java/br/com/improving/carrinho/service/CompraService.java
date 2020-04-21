package br.com.improving.carrinho.service;

import static br.com.improving.carrinho.MensagemEnum.CONFIGURACAO_SESSAO;
import static br.com.improving.carrinho.MensagemEnum.CONFIRMAR_CANCELAR_COMPRA;
import static br.com.improving.carrinho.MensagemEnum.DESEJA_CONTINUAR_COMPRANDO;
import static br.com.improving.carrinho.MensagemEnum.DIGITE_A_QUANTIDADE;
import static br.com.improving.carrinho.MensagemEnum.DIGITE_CODIGO_DO_PRODUTO;
import static br.com.improving.carrinho.MensagemEnum.DIGITE_NOME_CLIENTE;
import static br.com.improving.carrinho.MensagemEnum.DIGITE_O_PRECO;
import static br.com.improving.carrinho.MensagemEnum.ERRO_VALOR_UNITARIO;
import static br.com.improving.carrinho.MensagemEnum.MEDIA_DO_VALOR_DOS_CARRINHOS;
import static br.com.improving.carrinho.MensagemEnum.NAO_HA_CARRINHO_COM_COMPRAS;
import static br.com.improving.carrinho.MensagemEnum.PARA_ALTERAR_2;
import static br.com.improving.carrinho.MensagemEnum.PARA_CANCELAR_3;
import static br.com.improving.carrinho.MensagemEnum.PARA_CONFIRMAR_1;
import static br.com.improving.carrinho.MensagemEnum.RESUMO_DOS_CARRINHOS;
import static br.com.improving.carrinho.MensagemEnum.SAIR_DA_APLICACAO;
import static br.com.improving.carrinho.MensagemEnum.SESSAO_EXPIROU;
import static br.com.improving.carrinho.MensagemEnum.TOTAL_DO_PEDIDO;
import static br.com.improving.carrinho.MensagemEnum.VALOR_INCORRETO_DA_SESSAO;
import static br.com.improving.carrinho.utils.CompraUtils.imprimirMensagem;
import static br.com.improving.carrinho.utils.CompraUtils.linhaEmBranco;
import static br.com.improving.carrinho.utils.CompraUtils.linhaTracejada;
import static br.com.improving.carrinho.utils.CompraUtils.listaProdutos;
import static br.com.improving.carrinho.utils.CompraUtils.prompt;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalTime;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import br.com.improving.carrinho.CarrinhoComprasFactory;
import br.com.improving.carrinho.ItemException;
import br.com.improving.carrinho.MensagemEnum;
import br.com.improving.carrinho.Produto;
import br.com.improving.carrinho.utils.ProdutoDbUtils;

public class CompraService implements ICompra {
    private static final int ALTERAR_CARRINHO_2 = 2;
    private static final int CANCELAR_CARRINHO_3 = 3;
    private static final List<Produto> produtos = ProdutoDbUtils.findAll();

    private int sessao = 30;

    public void executar() {
        CarrinhoComprasFactory factory = new CarrinhoComprasFactory();
        Scanner ler = prompt();

        imprimirMensagem(CONFIGURACAO_SESSAO);

        try {
            sessao = ler.nextInt();
            sessao = sessao == 0 ? 30 : sessao;
        } catch (InputMismatchException e) {
            imprimirMensagem(VALOR_INCORRETO_DA_SESSAO);
        }

        iniciarCompra(factory);
        resumoDosCarrinhos(factory);

        ler.close();
    }

    private void iniciarCompra(final CarrinhoComprasFactory factory) {
        linhaEmBranco();
        imprimirMensagem(DIGITE_NOME_CLIENTE);

        String cliente = prompt().next();
        factory.criar(cliente);

        adicionarItem(factory, cliente);
        cancelarOuFinalizarCompra(factory, cliente);
        isSairDaAplicacao(factory);
    }

    private void adicionarItem(CarrinhoComprasFactory factory, String cliente) {
        linhaEmBranco();
        listaProdutos(produtos);
        linhaEmBranco();

        imprimirMensagem(DIGITE_CODIGO_DO_PRODUTO);
        Long codigoProduto = prompt().nextLong();

        try {
            final Produto produto = produtos.stream().filter(p -> p.getCodigo().equals(codigoProduto)).findFirst()
                    .orElse(null);

            imprimirMensagem(DIGITE_O_PRECO);
            BigDecimal valorUnitario = prompt().nextBigDecimal();

            imprimirMensagem(DIGITE_A_QUANTIDADE);
            int quantidade = prompt().nextInt();

            linhaEmBranco();
            factory.criar(cliente).adicionarItem(produto, valorUnitario, quantidade);
            continuarCompra(factory, cliente);
        } catch (ItemException ex) {
            imprimirMensagem(ex.getMessage());
            adicionarItem(factory, cliente);
        } catch (InputMismatchException ex) {
            linhaEmBranco();
            imprimirMensagem(ERRO_VALOR_UNITARIO);
            adicionarItem(factory, cliente);
        }

        linhaEmBranco();

    }

    private void cancelarOuFinalizarCompra(CarrinhoComprasFactory factory, String cliente) {
        if (factory.criar(cliente).getItens().isEmpty()) {
            factory.invalidar(cliente);
            return;
        }

        LocalTime inicioSessao = LocalTime.now();
        imprimirMensagem(CONFIRMAR_CANCELAR_COMPRA);
        linhaEmBranco();

        imprimirMensagem(PARA_CONFIRMAR_1);
        imprimirMensagem(PARA_ALTERAR_2);
        imprimirMensagem(PARA_CANCELAR_3);

        int acaoNoCarrinho = prompt().nextInt();
        boolean sessaoExpirou = isSessaoExpirou(inicioSessao, factory, cliente);

        if (acaoNoCarrinho == CANCELAR_CARRINHO_3 || sessaoExpirou) {
            factory.invalidar(cliente);
            linhaEmBranco();
        } else if (acaoNoCarrinho == ALTERAR_CARRINHO_2) {

            removerItem(factory, cliente);

        }
    }

    private void removerItem(CarrinhoComprasFactory factory, String cliente) {
        linhaEmBranco();
        listaProdutos(produtos);
        linhaEmBranco();

        imprimirMensagem(DIGITE_CODIGO_DO_PRODUTO);

        Long codigoProduto = prompt().nextLong();
        final Produto produto = produtos.stream().filter(p -> p.getCodigo().equals(codigoProduto)).findFirst()
                .orElse(null);
        try {
            factory.criar(cliente).removerItem(produto);
        } catch (Exception ex) {
            imprimirMensagem(ex.getMessage());
        }

        if (!factory.criar(cliente).getItens().isEmpty()) {
            imprimirMensagem(MensagemEnum.REMOVER_ITEM);
            String resposta = prompt().next();

            if (resposta.equalsIgnoreCase("S")) {
                removerItem(factory, cliente);
            }
        }else{
            factory.invalidar(cliente);
        }

    }

    private void resumoDosCarrinhos(CarrinhoComprasFactory factory) {

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

    private void continuarCompra(CarrinhoComprasFactory factory, String cliente) {
        imprimirMensagem(DESEJA_CONTINUAR_COMPRANDO);
        LocalTime inicioSessao = LocalTime.now();
        String continuarComprando = prompt().next();

        boolean isSessaoExpirou = isSessaoExpirou(inicioSessao, factory, cliente);

        if (!isSessaoExpirou) {
            if (continuarComprando.equalsIgnoreCase("S")) {
                adicionarItem(factory, cliente);
            }
        }
    }

    private boolean isSessaoExpirou(LocalTime inicioSessao, final CarrinhoComprasFactory factory,
            final String cliente) {
        if (Duration.between(inicioSessao, LocalTime.now()).getSeconds() > sessao) {
            if (factory.clienteCarrinho.isEmpty()) {
                factory.invalidar(cliente);
            }

            imprimirMensagem(SESSAO_EXPIROU);
            linhaEmBranco();

            factory.invalidar(cliente);

            return true;
        }

        inicioSessao = LocalTime.now();
        return false;
    }

    private void isSairDaAplicacao(CarrinhoComprasFactory factory) {
        linhaEmBranco();
        imprimirMensagem(SAIR_DA_APLICACAO);

        final String sair = prompt().next();

        if (sair.equalsIgnoreCase("N")) {
            iniciarCompra(factory);
        }
    }

}