package br.com.improving.carrinho;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Classe que representa o carrinho de compras de um cliente.
 */
public class CarrinhoCompras {

    /**
     * Permite a adição de um novo item no carrinho de compras.
     *
     * Caso o item já exista no carrinho para este mesmo produto, as seguintes regras deverão ser seguidas: 
     * - A quantidade do item deverá ser a soma da quantidade atual com a quantidade passada como parâmetro. 
     * - Se o valor unitário informado for diferente do valor unitário atual
     * do item, o novo valor unitário do item deverá ser o passado como parâmetro.
     *
     * Devem ser lançadas subclasses de RuntimeException caso não seja possível adicionar o item ao
     * carrinho de compras.
     *
     * @param produto
     * @param valorUnitario
     * @param quantidade
     */

    private List<Item> items = new ArrayList<>();

    public void adicionarItem(Produto produto, BigDecimal valorUnitario, int quantidade) {
        validaItem(produto, valorUnitario, quantidade);

        Optional<Item> itemExistente = items.stream().filter(i -> i.getProduto().equals(produto)).findFirst();

        if (itemExistente.isPresent()) {
            Item newItem = itemExistente.get();
            newItem.setQuantidade(newItem.getQuantidade() + quantidade);
            newItem.setValorUnitario(valorUnitario);
        } else {
            items.add(new Item(produto, valorUnitario, quantidade));
        }
    }

    /**
     * Permite a remoção do item que representa este produto do carrinho de compras.
     *
     * @param produto
     * @return Retorna um boolean, tendo o valor true caso o produto exista no carrinho de compras e
     *         false caso o produto não exista no carrinho.
     */
    public boolean removerItem(Produto produto) {
        validaProduto(produto);
        return items.removeIf(i -> i.getProduto() == produto);
    }

    /**
     * Permite a remoção do item de acordo com a posição. Essa posição deve ser determinada pela
     * ordem de inclusão do produto na coleção, em que zero representa o primeiro item.
     *
     * @param posicaoItem
     * @return Retorna um boolean, tendo o valor true caso o produto exista no carrinho de compras e
     *         false caso o produto não exista no carrinho.
     */
    public boolean removerItem(int posicaoItem) {
        if (!items.isEmpty()) {
            items.removeIf(i -> items.indexOf(i) == posicaoItem);
            return true;
        }
        return false;
    }

    /**
     * Retorna o valor total do carrinho de compras, que deve ser a soma dos valores totais de todos
     * os itens que compõem o carrinho.
     *
     * @return BigDecimal
     */
    public BigDecimal getValorTotal() {
        return items.stream().map(Item::getValorTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Retorna a lista de itens do carrinho de compras.
     *
     * @return itens
     */
    public Collection<Item> getItens() {
        return items;
    }

    public void validaItem(Produto produto, BigDecimal valorUnitario, int quantidade) {
        validaProduto(produto);

        if (quantidade <= 0) {
            throw new ItemException("ERRO: [A quantidade deve ter um valor inteiro maior que zero]");

        }

        if (valorUnitario.signum() <= 0) {
            throw new ItemException("ERRO: [O preço unitário deve ser um valor maior que zero]");
        }
    }

    public void validaProduto(Produto produto) {
        if (produto == null) {
            throw new ItemException("ERRO: [Produto nao encontrado com o código informado]");
        }
    }

}
