package com.example.backend.dto;

import java.math.BigDecimal;

/**
 * DTO para transporte de dados de Benefício.
 * Contém informações de nome, descrição, valor e status de ativação.
 */
public record BeneficioDTO(
    Long id, 
    String nome, 
    String descricao, 
    BigDecimal valor, 
    Boolean ativo,
    Long version) {

        public BeneficioDTO {
            if (ativo == null) {
                ativo = true;
            }
            if (valor == null) {
                valor = BigDecimal.ZERO;
            }
        }
}