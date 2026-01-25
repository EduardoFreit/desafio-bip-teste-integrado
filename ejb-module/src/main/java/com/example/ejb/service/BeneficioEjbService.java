package com.example.ejb.service;

import java.math.BigDecimal;

import com.example.ejb.exception.ContaNaoEncontradaException;
import com.example.ejb.exception.SaldoInsuficienteException;
import com.example.ejb.model.Beneficio;

import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;

@Stateless
@Slf4j
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class BeneficioEjbService {

    @PersistenceContext
    private EntityManager em;

    public void transferir(Long contaOrigemId, Long contaDestinoId, BigDecimal valor) 
        throws SaldoInsuficienteException, ContaNaoEncontradaException, OptimisticLockException {
        
        try {

            // 1. Validações iniciais
            validacaoInicialTransferencia(contaOrigemId, contaDestinoId, valor);

            // 2. Buscar entidades (sem lock ainda)
            Beneficio origem = em.find(Beneficio.class, contaOrigemId, LockModeType.OPTIMISTIC);
            Beneficio destino = em.find(Beneficio.class, contaDestinoId, LockModeType.OPTIMISTIC);
            
            if (origem == null) {
                log.error("Conta de origem não encontrada: {}", contaOrigemId);
                throw new ContaNaoEncontradaException();
            }

            if (destino == null) {
                log.error("Conta de destino não encontrada: {}", contaDestinoId);
                throw new ContaNaoEncontradaException();
            }
            
            // 3. Validar saldo
            if (origem.getValor().compareTo(valor) < 0) {
                log.error("Saldo insuficiente na conta de origem: origemId={}, saldo={}, valorSolicitado={}", 
                        contaOrigemId, origem.getValor(), valor);
                throw new SaldoInsuficienteException("Saldo insuficiente");
            }
            
            // 5. Executar transferência
            origem.setValor(origem.getValor().subtract(valor));
            destino.setValor(destino.getValor().add(valor));

            // Força execução e detecção de conflito de versão
            em.flush();

            log.info("Transferência realizada com sucesso: origemId={}, destinoId={}, valor={}, " +
                        "novoSaldoOrigem={}, novoSaldoDestino={}", 
                        contaOrigemId, contaDestinoId, valor, origem.getValor(), destino.getValor());
        } catch (OptimisticLockException e) {
            log.warn("Conflito de versão detectado na transferência", e);
            throw e;
        }
    }

    private void validacaoInicialTransferencia(Long contaOrigemId, Long contaDestinoId, BigDecimal valor) {
        if (contaOrigemId == null) {
            log.error("Conta de origem inválida: null");
            throw new IllegalArgumentException("Conta de origem inválida");
        }

        if (contaDestinoId == null) {
            log.error("Conta de destino inválida: null");
            throw new IllegalArgumentException("Conta de destino inválida");
        }

        if (contaOrigemId.equals(contaDestinoId)) {
            log.error("Conta de origem e destino são iguais: {}", contaOrigemId);
            throw new IllegalArgumentException("Conta de origem e destino não podem ser iguais");
        }

        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            log.error("Valor de transferência inválido: {}", valor);
            throw new IllegalArgumentException("Valor deve ser positivo");
        }
    }
}
