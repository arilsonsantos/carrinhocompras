package br.com.improving.carrinho;

public class ItemException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ItemException(String message) {
        super(message);
    }

    public ItemException() {
        super();
    }

}