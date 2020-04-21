package br.com.improving.carrinho.service;

import static br.com.improving.carrinho.utils.CompraUtils.imprimirMensagem;
import static br.com.improving.carrinho.utils.CompraUtils.linhaEmBranco;
import static br.com.improving.carrinho.utils.CompraUtils.linhaTracejada;
import static br.com.improving.carrinho.utils.CompraUtils.listaProdutos;
import static br.com.improving.carrinho.utils.CompraUtils.prompt;
import static br.com.improving.enumerates.MensagemEnum.CONFIGURACAO_SESSAO;
import static br.com.improving.enumerates.MensagemEnum.CONFIRMAR_CANCELAR_COMPRA;
import static br.com.improving.enumerates.MensagemEnum.DESEJA_CONTINUAR_COMPRANDO;
import static br.com.improving.enumerates.MensagemEnum.DIGITE_A_QUANTIDADE;
import static br.com.improving.enumerates.MensagemEnum.DIGITE_CODIGO_DO_PRODUTO;
import static br.com.improving.enumerates.MensagemEnum.DIGITE_NOME_CLIENTE;
import static br.com.improving.enumerates.MensagemEnum.DIGITE_O_PRECO;
import static br.com.improving.enumerates.MensagemEnum.MEDIA_DO_VALOR_DOS_CARRINHOS;
import static br.com.improving.enumerates.MensagemEnum.NAO_HA_CARRINHO_COM_COMPRAS;
import static br.com.improving.enumerates.MensagemEnum.PARA_ALTERAR_2;
import static br.com.improving.enumerates.MensagemEnum.PARA_CANCELAR_3;
import static br.com.improving.enumerates.MensagemEnum.PARA_CONFIRMAR_1;
import static br.com.improving.enumerates.MensagemEnum.RESUMO_DOS_CARRINHOS;
import static br.com.improving.enumerates.MensagemEnum.SAIR_DA_APLICACAO;
import static br.com.improving.enumerates.MensagemEnum.SESSAO_EXPIROU;
import static br.com.improving.enumerates.MensagemEnum.TOTAL_DO_PEDIDO;
import static br.com.improving.enumerates.MensagemEnum.VALOR_INCORRETO_DA_SESSAO;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalTime;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import br.com.improving.carrinho.CarrinhoComprasFactory;
import br.com.improving.carrinho.ItemException;
import br.com.improving.carrinho.Produto;
import br.com.improving.carrinho.utils.ProdutoDbUtils;
import br.com.improving.enumerates.MensagemEnum;
import br.com.improving.enumerates.MensagemErroEnum;

/*
* Classe responsável por realizar toda a movimentação de compras de um ou mais carrinhos.
*/
public class CompraService implements ICompra {
    private static final int ALTERAR_CARRINHO_2 = 2;
    private static final int CANCELAR_CARRINHO_3 = 3;
    private static final List<Produto> produtos = ProdutoDbUtils.findAll();

    private int sessao = 30;

    /**
    * Configuração da sessão e invocação do início das compras.
    */
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

    /**
     * Início das compras, onde o carrinho de compras é criado e associado a um cliente.
     * 
     * @param factory
     */
    private void iniciarCompra(final CarrinhoComprasFactory factory) {
        linhaEmBranco();
        imprimirMensagem(DIGITE_NOME_CLIENTE);

        String cliente = prompt().next();
        factory.criar(cliente);

        adicionarItem(factory, cliente);
        cancelarOuFinalizarCompra(factory, cliente);
        isSairDaAplicacao(factory);
    }

    /**
     * Adiciona item(s) ao carrinho de compras.
     * 
     * @param factory
     * @param cliente
     */
    private void adicionarItem(CarrinhoComprasFactory factory, String cliente) {
        linhaEmBranco();
        listaProdutos(produtos);
        linhaEmBranco();

        imprimirMensagem(DIGITE_CODIGO_DO_PRODUTO);
        Long codigoProduto = prompt().nextLong();

        try {
            Produto produto = produtos.stream().filter(p -> p.getCodigo().equals(codigoProduto)).findFirst()
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
            imprimirMensagem(MensagemErroEnum.PRECO_UNITARIO_INVALIDO);
            adicionarItem(factory, cliente);
        }

        linhaEmBranco();

    }

    /**
     * Opções para confirmar, alterar ou cancelar uma compra.
     * 
     * @param factory
     * @param cliente
     */
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

    /**
     * Remove item(s) do carrinho de compras.
     * 
     * @param factory
     * @param cliente
     */
    private void removerItem(CarrinhoComprasFactory factory, String cliente) {
        linhaEmBranco();
        listaProdutos(produtos);
        linhaEmBranco();

        imprimirMensagem(DIGITE_CODIGO_DO_PRODUTO);

        Long codigoProduto = prompt().nextLong();
        Produto produto = produtos.stream().filter(p -> p.getCodigo().equals(codigoProduto)).findFirst()
                .orElse(null);
        try {
            factory.criar(cliente).removerItem(produto);
        } catch (Exception ex) {
            imprimirMensagem(ex.getMessage());
        }

        if (!factory.criar(cliente).getItens().isEmpty()) {
            linhaEmBranco();
            imprimirMensagem(MensagemEnum.REMOVER_ITEM);
            String resposta = prompt().next();

            if (resposta.equalsIgnoreCase("S")) {
                removerItem(factory, cliente);
            }
        }else{
            factory.invalidar(cliente);
        }

    }

    /**
     * Antes de sair da aplicação, um resumo das compras será exibido.
     * 
     * @param factory
     */
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

    /**
     * Questiona o cliente se quer continuar adicionando item(s) ao carrinho de compras.
     * 
     * @param factory
     * @param cliente
     */
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

    /**
     * Verifica se a sessão do usuário expirou.
     * 
     * @param inicioSessao
     * @param factory
     * @param cliente
     * @return boolean
     */
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

    /**
     * Pergunta ao usuário se deseja sair da aplicação.
     * 
     * Se não, deve voltar ao início das compras.
     * 
     * @param factory
     */
    private void isSairDaAplicacao(CarrinhoComprasFactory factory) {
        linhaEmBranco();
        imprimirMensagem(SAIR_DA_APLICACAO);

        final String sair = prompt().next();

        if (sair.equalsIgnoreCase("N")) {
            iniciarCompra(factory);
        }
    }

}