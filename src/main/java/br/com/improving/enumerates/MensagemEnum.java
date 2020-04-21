package br.com.improving.enumerates;

/**
 * Enum com mensagens diversas
 */
public enum MensagemEnum {
    CONFIGURACAO_SESSAO("Digite o tempo da sessão (em segundos) ou digite zero para o manter o valor padrão igual a 30."),
    VALOR_INCORRETO_DA_SESSAO("O valor inserido não está correo. A sessão teráo o tempo padrão, 30s. "),
    DIGITE_NOME_CLIENTE("Digite o nome do cliente:"), 
    CONFIRMAR_CANCELAR_COMPRA("Escolha uma das opções abaixo:"),
    PARA_CONFIRMAR_1("1 - Para confimar a compra; "), 
    PARA_ALTERAR_2("2 - Para REMOVER um item da compra"),
    PARA_CANCELAR_3("3 - Para cancelar a compra; "),
    RESUMO_DOS_CARRINHOS("RESUMO DOS CARRINHOS: "), 
    TOTAL_DO_PEDIDO("Total do pedido: "),
    NAO_HA_CARRINHO_COM_COMPRAS("Não há carrinho com compras. "),
    MEDIA_DO_VALOR_DOS_CARRINHOS("Média do valor dos carrinhos:  "),
    DIGITE_CODIGO_DO_PRODUTO("Digite o código do produto: "), 
    DIGITE_O_PRECO("Digite o preço: "),
    DIGITE_A_QUANTIDADE("Digite a quantidade: "), 
    REMOVER_ITEM("Deseja remover outro item? [S/N]"),
    DESEJA_CONTINUAR_COMPRANDO("Deseja colocar mais produtos neste carrinho?: [S/N]: "),
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