package br.com.improving.enumerates;

/**
 * Enum com mensagens de erros ou de validação
 */
public enum MensagemErroEnum {
    VALOR_INCORRETO_DA_SESSAO("O valor inserido não está correo. A sessão teráo o tempo padrão, 30s. "),
    QUANTIDADE_INVALIDA("ERRO: [A quantidade deve ter um valor inteiro maior que zero]"),
    PRECO_UNITARIO_INVALIDO("ERRO: [O preço unitário deve ser um valor maior que zero e conter <,> como separador decimal.]"),
    PRODUTO_NAO_ENCONTRADO("ERRO: [Produto nao encontrado com o código informado]");

    private String descricao;

    private MensagemErroEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

}