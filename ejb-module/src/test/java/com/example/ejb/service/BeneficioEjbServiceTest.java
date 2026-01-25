package com.example.ejb.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.ejb.model.Beneficio;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;

import com.example.ejb.exception.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("BeneficioEjbService - Testes Unitários")
class BeneficioEjbServiceTest {

    @Mock
    private EntityManager em;

    @InjectMocks
    private BeneficioEjbService service;

    private Beneficio contaOrigem;
    private Beneficio contaDestino;

    @BeforeEach
    void setUp() {
        contaOrigem = new Beneficio();
        contaOrigem.setId(1L);
        contaOrigem.setNome("Conta Origem");
        contaOrigem.setValor(new BigDecimal("1000.00"));
        contaOrigem.setVersion(1L);

        contaDestino = new Beneficio();
        contaDestino.setId(2L);
        contaDestino.setNome("Conta Destino");
        contaDestino.setValor(new BigDecimal("500.00"));
        contaDestino.setVersion(1L);
    }

    // TESTES DE SUCESSO

    @Test
    @DisplayName("Deve transferir com sucesso quando dados são válidos")
    void testTransferenciaComSucesso() throws Exception {
        // Arrange
        BigDecimal valor = new BigDecimal("100.00");
        when(em.find(eq(Beneficio.class), eq(1L), any(LockModeType.class)))
            .thenReturn(contaOrigem);
        when(em.find(eq(Beneficio.class), eq(2L), any(LockModeType.class)))
            .thenReturn(contaDestino);

        // Act
        service.transferir(1L, 2L, valor);

        // Assert
        assertEquals(new BigDecimal("900.00"), contaOrigem.getValor());
        assertEquals(new BigDecimal("600.00"), contaDestino.getValor());
        verify(em, times(1)).flush();
    }

    // TESTES DE FALHA
    @Test
    @DisplayName("Deve lançar SaldoInsuficienteException quando saldo da conta de origem é insuficiente")
    void testTransferenciaSaldoInsuficiente() throws Exception {
        // Arrange
        BigDecimal valor = new BigDecimal("1500.00");
        when(em.find(eq(Beneficio.class), eq(1L), any(LockModeType.class)))
            .thenReturn(contaOrigem);
        when(em.find(eq(Beneficio.class), eq(2L), any(LockModeType.class)))
            .thenReturn(contaDestino);

        // Act & Assert
        assertThrows(SaldoInsuficienteException.class, () -> {
            service.transferir(1L, 2L, valor);
        });

        // Verifica que os saldos não foram alterados
        assertEquals(new BigDecimal("1000.00"), contaOrigem.getValor());
        assertEquals(new BigDecimal("500.00"), contaDestino.getValor());
        verify(em, never()).flush();
    }

    @Test
    @DisplayName("Deve lançar ContaNaoEncontradaException quando conta de origem não existe")
    void testTransferenciaContaOrigemNaoEncontrada() throws Exception {
        // Arrange
        BigDecimal valor = new BigDecimal("100.00");
        when(em.find(eq(Beneficio.class), eq(1L), any(LockModeType.class)))
            .thenReturn(null); // Conta origem não encontrada
        when(em.find(eq(Beneficio.class), eq(2L), any(LockModeType.class)))
            .thenReturn(contaDestino);

        // Act & Assert
        assertThrows(ContaNaoEncontradaException.class, () -> {
            service.transferir(1L, 2L, valor);
        });

        // Verifica que os saldos não foram alterados
        assertEquals(new BigDecimal("500.00"), contaDestino.getValor());
        verify(em, never()).flush();
    }

    @Test
    @DisplayName("Deve lançar ContaNaoEncontradaException quando conta de destino não existe")
    void testTransferenciaContaDestinoNaoEncontrada() throws Exception {
        // Arrange
        BigDecimal valor = new BigDecimal("100.00");
        when(em.find(eq(Beneficio.class), eq(1L), any(LockModeType.class)))
            .thenReturn(contaOrigem);
        when(em.find(eq(Beneficio.class), eq(2L), any(LockModeType.class)))
            .thenReturn(null); // Conta destino não encontrada

        // Act & Assert
        assertThrows(ContaNaoEncontradaException.class, () -> {
            service.transferir(1L, 2L, valor);
        });

        // Verifica que os saldos não foram alterados
        assertEquals(new BigDecimal("1000.00"), contaOrigem.getValor());
        verify(em, never()).flush();
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException quando valor de transferência é nulo")
    void testTransferenciaValorNulo() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            service.transferir(1L, 2L, null);
        });
        verify(em, never()).flush();
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException quando conta de origem é nula")
    void testTransferenciaContaOrigemNula() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            service.transferir(null, 2L, new BigDecimal("100.00"));
        });
        verify(em, never()).flush();    
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException quando conta de destino é nula")
    void testTransferenciaContaDestinoNula() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            service.transferir(1L, null, new BigDecimal("100.00"));
        });
        verify(em, never()).flush();    
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException quando contas são iguais")
    void testTransferenciaContasIguais() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            service.transferir(1L, 1L, new BigDecimal("100.00"));
        });
        verify(em, never()).flush();    
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException quando valor é zero")
    void testTransferenciaValorZero() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            service.transferir(1L, 2L, BigDecimal.ZERO);
        });
        verify(em, never()).flush();    
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException quando valor é negativo")
    void testTransferenciaValorNegativo() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            service.transferir(1L, 2L, new BigDecimal("-100.00"));
        });
        verify(em, never()).flush();    
    }

}
