package br.com.improving.carrinho;


public enum MensagemEnum {
    DIGITE_NOME_CLIENTE("Digite o nome do cliente:"), 
    CONFIRMAR_CANCELAR_COMPRA("Escolha uma das opções abaixo:"),
    PARA_CONFIRMAR_1("1 - Para confimar a compra; "), 
    PARA_CANCELAR_2("2 - Para cancelar a compra; "),
    RESUMO_DOS_CARRINHOS("RESUMO DOS CARRINHOS: "), 
    TOTAL_DO_PEDIDO("Total do pedido: "),
    NAO_HA_CARRINHO_COM_COMPRAS("Não há carrinho com compras. "),
    MEDIA_DO_VALOR_DOS_CARRINHOS("Média do valor dos carrinhos:  "),
    DIGITE_CODIGO_DO_PRODUTO("Digite o código do produto: "), 
    DIGITE_O_PRECO("Digite o preço: "),
    DIGITE_A_QUANTIDADE("Digite a quantidade: "), 
    DESEJA_CONTINUAR_COMPRANDO("Deseja continuar comprando?: [S/N]: "),
    RESUMO_CARRINHO_CLIENTE("RESUMO DO CARRINHO DO(A) CLIENTE: "), 
    SESSAO_EXPIROU("Sessão expirou!"),
    SAIR_DA_APLICACAO("Sair da aplicação? [S/N]: ");

    private String descricao;

    private MensagemEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

}