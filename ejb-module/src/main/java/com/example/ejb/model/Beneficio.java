package com.example.ejb.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Entidade que representa uma conta/benefício.
 * @author lepf9
 */
@Entity
@Data
@Table(name = "BENEFICIO")
public class Beneficio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message="Nome do benefício é obrigatório")
    @Size(min = 1, max = 100, message="Nome do benefício deve ter entre 1 e 100 caracteres")
    @Column(nullable = false)
    private String nome;

    @Size(max = 255, message="Descrição do benefício deve ter no máximo 255 caracteres")
    private String descricao;

    @NotNull(message="Valor do benefício é obrigatório")
    @PositiveOrZero(message="O valor do benefício deve ser positivo ou zero")
    @Column(nullable = false, precision = 15, scale = 2)
    @DecimalMax(value = "999999999999999.99", message = "O valor do benefício não pode exceder 999.999.999.999.999.99")
    private BigDecimal valor;

    @Column(nullable = false)
    private Boolean ativo = true;

    @Version
    @Column(nullable = false)
    private Long version = 0L;
}
