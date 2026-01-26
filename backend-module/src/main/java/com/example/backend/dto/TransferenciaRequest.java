package com.example.backend.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Objeto para transporte de dados de transferência.
 * Contém informações de conta de origem, conta de destino e valor da transferência.
 * @author lepf9
 */
public record TransferenciaRequest(
    @NotNull(message = "O benefício de origem da transferência é obrigatório") Long contaOrigemId,
    @NotNull(message = "O benefício de destino da transferência é obrigatório") Long contaDestinoId,
    @NotNull(message = "O valor da transferencia é obrigatório") @Positive(message = "O valor da transferência deve ser maior que zero") BigDecimal valor
) {}
