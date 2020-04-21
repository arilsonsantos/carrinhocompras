package br.com.improving.carrinho.service;

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
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import br.com.improving.carrinho.CarrinhoComprasFactory;
import br.com.improving.carrinho.ItemException;
import br.com.improving.carrinho.MensagemEnum;
import br.com.improving.carrinho.Produto;
import br.com.improving.carrinho.utils.ProdutoDbUtils;

public class CompraService implements ICompra {
    private static final int CANCELAR_CARRINHO_2 = 2;
    private static final List<Produto> produtos = ProdutoDbUtils.findAll();

    private Long codigoProduto = null;
    private String cliente = "";
    private int sessao = 30;

    public void executar() {
        CarrinhoComprasFactory factory = new CarrinhoComprasFactory();
        Scanner ler = prompt();

        imprimirMensagem(
                "Digite o tempo da sessão (em segundos) ou digite zero para o manter o valor padrão igual a 30.");

        sessao = ler.nextInt() == 0 ? 30 : sessao;

        iniciarCompra(factory);
        ler.close();
    }

    private void iniciarCompra(final CarrinhoComprasFactory factory) {
        linhaEmBranco();
        imprimirMensagem(DIGITE_NOME_CLIENTE);

        cliente = prompt().next();
        factory.criar(cliente);

        adicionaItemAoCarrinho(factory, cliente);

        cancelarOuFinalizarCompra(factory);
        isSairDaAplicacao(factory);
    }

    private void cancelarOuFinalizarCompra(CarrinhoComprasFactory factory) {
        if (factory.criar(cliente).getItens().isEmpty()) {
            factory.invalidar(cliente);
            return;
        }

        final LocalTime inicioSessao = LocalTime.now();
        imprimirMensagem(CONFIRMAR_CANCELAR_COMPRA);
        linhaEmBranco();

        imprimirMensagem(PARA_CONFIRMAR_1);
        imprimirMensagem(PARA_CANCELAR_2);

        final int cancelarCarrinho = prompt().nextInt();
        final boolean sessaoExpirou = isSessaoExpirou(inicioSessao, factory, cliente);

        if (cancelarCarrinho == CANCELAR_CARRINHO_2 || sessaoExpirou) {
            factory.invalidar(cliente);
            linhaEmBranco();
        }
    }

    private void resumoDosCarrinhos(final CarrinhoComprasFactory factory) {
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

    private void adicionaItemAoCarrinho(CarrinhoComprasFactory factory, String cliente) {
        linhaEmBranco();
        listaProdutos(produtos);
        linhaEmBranco();

        imprimirMensagem(DIGITE_CODIGO_DO_PRODUTO);
        codigoProduto = prompt().nextLong();

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
            adicionaItemAoCarrinho(factory, cliente);
        } catch (InputMismatchException ex) {
            linhaEmBranco();
            imprimirMensagem(MensagemEnum.ERRO_VALOR_UNITARIO);
            adicionaItemAoCarrinho(factory, cliente);
        }

        linhaEmBranco();

    }

    private void continuarCompra(CarrinhoComprasFactory factory, String cliente) {
        imprimirMensagem(DESEJA_CONTINUAR_COMPRANDO);
        LocalTime inicioSessao = LocalTime.now();
        String continuarComprando = prompt().next();

        boolean isSessaoExpirou = isSessaoExpirou(inicioSessao, factory, cliente);

        if (isSessaoExpirou) {
            if (continuarComprando.equalsIgnoreCase("N")) {
                imprimirMensagem(RESUMO_CARRINHO_CLIENTE, cliente);
                linhaTracejada();

                if (!factory.criar(cliente).getItens().isEmpty()) {
                    factory.criar(cliente).getItens().forEach(System.out::println);
                    imprimirMensagem(TOTAL_DO_PEDIDO, factory.criar(cliente).getValorTotal().toString());
                    linhaEmBranco();
                }
            }
        } else {
            if (continuarComprando.equalsIgnoreCase("S")) {
                adicionaItemAoCarrinho(factory, cliente);
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
            isSairDaAplicacao(factory);

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
            isSairDaAplicacao(factory);
        } else {
            resumoDosCarrinhos(factory);
            System.exit(0);
        }
    }

    private Scanner prompt() {
        return new Scanner(System.in);
    }

    private void listaProdutos(final List<Produto> produtos) {
        produtos.forEach(System.out::println);
    }

    private void linhaTracejada() {
        System.out.println("---------------------------------------------------------------");
    }

    private void linhaEmBranco() {
        System.out.println();
    }

    private void imprimirMensagem(final String mensagem) {
        System.out.println(mensagem);
    }

    private void imprimirMensagem(final MensagemEnum mensagemEnum) {
        System.out.println(mensagemEnum.getDescricao());
    }

    private void imprimirMensagem(final MensagemEnum mensagemEnum, String complemento) {
        System.out.println(mensagemEnum.getDescricao() + complemento);
    }

}