package com.example.ejb.service;

import java.math.BigDecimal;

import com.example.ejb.exception.ContaInativaException;
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
        throws SaldoInsuficienteException, ContaNaoEncontradaException, OptimisticLockException, ContaInativaException {
        
        try {

            validacaoInicialTransferir(contaOrigemId, contaDestinoId, valor);

            Beneficio origem = em.find(Beneficio.class, contaOrigemId, LockModeType.OPTIMISTIC);
            Beneficio destino = em.find(Beneficio.class, contaDestinoId, LockModeType.OPTIMISTIC);
            
            validacaoFinalTransferir(origem, destino, valor, contaOrigemId, contaDestinoId);
            
            origem.setValor(origem.getValor().subtract(valor));
            destino.setValor(destino.getValor().add(valor));

            em.flush();

            log.info("Transferência realizada com sucesso: origemId={}, destinoId={}, valor={}, " +
                        "novoSaldoOrigem={}, novoSaldoDestino={}", 
                        contaOrigemId, contaDestinoId, valor, origem.getValor(), destino.getValor());
        } catch (OptimisticLockException e) {
            log.warn("Conflito de versão detectado na transferência", e);
            throw e;
        }
    }

    private void validacaoInicialTransferir(Long contaOrigemId, Long contaDestinoId, BigDecimal valor) {
        if (contaOrigemId == null) {
            log.error("Conta de origem inválida: null");
            throw new IllegalArgumentException("Id da conta de origem inválida");
        }

        if (contaDestinoId == null) {
            log.error("Conta de destino inválida: null");
            throw new IllegalArgumentException("Id da conta de destino inválida");
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

    private void validacaoFinalTransferir(Beneficio origem, Beneficio destino, BigDecimal valor, Long contaOrigemId, Long contaDestinoId) 
        throws SaldoInsuficienteException, ContaNaoEncontradaException, ContaInativaException {
        if (origem == null) {
            log.error("Conta de origem não encontrada: {}", contaOrigemId);
            throw new ContaNaoEncontradaException("Conta de origem não encontrada");
        }

        if (destino == null) {
            log.error("Conta de destino não encontrada: {}", contaDestinoId);
            throw new ContaNaoEncontradaException("Conta de destino não encontrada");
        }

        if (origem.getAtivo() == null || !origem.getAtivo()) {
            log.error("Conta de origem inativa: {}", contaOrigemId);
            throw new ContaInativaException("Conta de origem inativa");
        }

        if (destino.getAtivo() == null || !destino.getAtivo()) {
            log.error("Conta de destino inativa: {}", contaDestinoId);
            throw new ContaInativaException("Conta de destino inativa");
        }
        
        if (origem.getValor().compareTo(valor) < 0) {
            log.error("Saldo insuficiente na conta de origem: origemId={}, saldo={}, valorSolicitado={}", 
                    contaOrigemId, origem.getValor(), valor);
            throw new SaldoInsuficienteException("Saldo insuficiente");
        }
    }
}
