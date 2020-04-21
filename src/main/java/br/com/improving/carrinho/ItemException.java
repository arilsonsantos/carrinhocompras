package br.com.improving.carrinho;

import br.com.improving.enumerates.MensagemErroEnum;

/**
 * Classe de exceção de um item
 */
public class ItemException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ItemException(String message) {
        super(message);
    }

    public ItemException(MensagemErroEnum message) {
        super(message.getDescricao());
    }

    public ItemException() {
        super();
    }

}