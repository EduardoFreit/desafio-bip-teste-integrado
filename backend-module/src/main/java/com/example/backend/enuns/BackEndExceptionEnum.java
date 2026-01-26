
package com.example.backend.enuns;

/**
 *
 * Enum para tipos de exceções no backend
 * @author lepf9
 */
public enum BackEndExceptionEnum {
    ERRO_GENERICO("Erro genérico no backend"),
    ERRO_AO_LISTAR_BENEFICIOS("Erro ao listar benefícios"),
    ERRO_AO_BUSCAR_BENEFICIO_POR_ID("Erro ao buscar benefício por ID"),
    BENEFICIO_NAO_ENCONTRADO("Benefício não encontrado"),
    ERRO_AO_CRIAR_BENEFICIO("Erro ao criar benefício"),
    ERRO_AO_ATUALIZAR_BENEFICIO("Erro ao atualizar benefício"),
    ERRO_AO_DELETAR_BENEFICIO("Erro ao deletar benefício"),
    ERRO_AO_TRANSFERIR_VALOR_ENTRE_BENEFICIOS("Erro ao transferir valor entre benefícios"),
    ERRO_AO_TRANSFERIR_ARGUMENTO_INVALIDO("Erro ao transferir: argumento inválido"),
    ERRO_AO_TRANSFERIR_SALDO_INSUFICIENTE("Erro ao transferir: saldo insuficiente"),
    ERRO_AO_TRANSFERIR_CONTA_INATIVA("Erro ao transferir: conta inativa"),
    ERRO_AO_TRANSFERIR_CONTA_NAO_ENCONTRADA("Erro ao transferir: conta não encontrada"),
    ERRO_AO_TRANSFERIR_CONFLITO_VERSAO("Erro ao transferir: conflito de versão");


    private final String mensagem;

    BackEndExceptionEnum(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getMensagem() {
        return mensagem;
    }

}
