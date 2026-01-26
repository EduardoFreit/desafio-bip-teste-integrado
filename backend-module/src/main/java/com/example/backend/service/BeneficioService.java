package com.example.backend.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.backend.dto.BeneficioDTO;
import com.example.backend.dto.TransferenciaRequest;
import com.example.backend.exception.BackendException;
import com.example.backend.mapper.interfaces.IBeneficioMapper;
import com.example.backend.repository.BeneficioRepository;
import com.example.backend.service.interfaces.IBeneficioService;
import com.example.backend.specification.BeneficioSpec;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;

import com.example.backend.enuns.BackEndExceptionEnum;
import com.example.ejb.exception.ContaInativaException;
import com.example.ejb.exception.ContaNaoEncontradaException;
import com.example.ejb.exception.SaldoInsuficienteException;
import com.example.ejb.model.Beneficio;
import com.example.ejb.service.BeneficioEjbService;

import jakarta.persistence.OptimisticLockException;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * Serviço para operações relacionadas a Benefício
 * @author lepf9
 */
@Service
@Transactional
@Slf4j
public class BeneficioService implements IBeneficioService {

    private final BeneficioRepository beneficioRepository;
    private final BeneficioEjbService beneficioEjbService;
    private final IBeneficioMapper beneficioMapper;
    private final Validator validator;

    public BeneficioService(BeneficioRepository beneficioRepository, BeneficioEjbService beneficioEjbService, IBeneficioMapper beneficioMapper, Validator validator) {
        this.beneficioRepository = beneficioRepository;
        this.beneficioEjbService = beneficioEjbService;
        this.beneficioMapper = beneficioMapper;
        this.validator = validator;
    }

    @Override
    public Page<BeneficioDTO> listar(Pageable pageable, String nome, Boolean ativo) {
        try {
            Specification<Beneficio> spec = Specification.where(BeneficioSpec.nomeLike(nome))
                                               .and(BeneficioSpec.ativoIgual(ativo));
            Page<Beneficio> page = beneficioRepository.findAll(spec, pageable);
            return page.map(beneficioMapper::beneficioParaBeneficioDTO);
        } catch (Exception e) {
            log.error("Erro ao listar benefícios: {}", e.getMessage());
            throw new BackendException("Erro ao listar benefícios", BackEndExceptionEnum.ERRO_AO_LISTAR_BENEFICIOS);
        }
    }

    @Override
    public BeneficioDTO criar(BeneficioDTO beneficioDto) {
        try {
            Beneficio beneficioCriar = beneficioMapper.beneficioDTOParaBeneficio(beneficioDto);

            validarObjeto(beneficioCriar);
            Beneficio beneficioSalvar = beneficioRepository.save(beneficioCriar);

            return beneficioMapper.beneficioParaBeneficioDTO(beneficioSalvar);
        } catch (BackendException e) {
            throw e;
        } catch (Exception e) {
            log.error("Erro ao criar benefício: {}", e.getMessage());
            throw new BackendException("Erro ao criar benefício", BackEndExceptionEnum.ERRO_AO_CRIAR_BENEFICIO);
        }
    }

    @Override
    public BeneficioDTO buscarPorId(Long id) {
        try {
            Beneficio beneficio = beneficioRepository.findById(id).orElseThrow(() -> new BackendException("Benefício não encontrado", HttpStatus.NOT_FOUND, BackEndExceptionEnum.BENEFICIO_NAO_ENCONTRADO));
            return beneficioMapper.beneficioParaBeneficioDTO(beneficio);
        } catch (BackendException e) {
            throw e;
        } catch (Exception e) {
            log.error("Erro ao buscar benefício({}): {}", id, e.getMessage());
            throw new BackendException("Erro ao buscar benefício", BackEndExceptionEnum.ERRO_AO_BUSCAR_BENEFICIO_POR_ID);
        }
    }

    @Override
    public BeneficioDTO atualizar(Long id, BeneficioDTO beneficioDto) {
        try {

            Beneficio beneficioAtualizar = beneficioRepository.findById(id).orElseThrow(() -> new BackendException("Benefício não encontrado", HttpStatus.NOT_FOUND, BackEndExceptionEnum.BENEFICIO_NAO_ENCONTRADO));

            beneficioAtualizar.setNome(beneficioDto.nome());
            beneficioAtualizar.setDescricao(beneficioDto.descricao());
            beneficioAtualizar.setAtivo(beneficioDto.ativo());

            validarObjeto(beneficioAtualizar);
            Beneficio beneficioAtualizado = beneficioRepository.save(beneficioAtualizar);

            return beneficioMapper.beneficioParaBeneficioDTO(beneficioAtualizado);
        } catch (BackendException e) {
            throw e;
        } catch (Exception e) {
            log.error("Erro ao atualizar benefício({}): {}", id, e.getMessage());
            throw new BackendException("Erro ao atualizar benefício", BackEndExceptionEnum.ERRO_AO_ATUALIZAR_BENEFICIO);
        }
    }

    @Override
    public void deletar(Long id) {
        try {
            if (!beneficioRepository.existsById(id)) {
                throw new BackendException("Benefício não encontrado", HttpStatus.NOT_FOUND, BackEndExceptionEnum.BENEFICIO_NAO_ENCONTRADO);
            }
            beneficioRepository.deleteById(id);
        } catch (BackendException e) {
            throw e;
        } catch (Exception e) {
            log.error("Erro ao deletar benefício({}): {}", id, e.getMessage());
            throw new BackendException("Erro ao deletar benefício", BackEndExceptionEnum.ERRO_AO_DELETAR_BENEFICIO);
        }
    }

    @Override
    public void transferir(TransferenciaRequest request) {
        try {
            validarObjeto(request);
            beneficioEjbService.transferir(request.contaOrigemId(), request.contaDestinoId(), request.valor());
        } catch (BackendException e) {
            throw new BackendException(e.getMessage(), e.getStatus(), BackEndExceptionEnum.ERRO_AO_TRANSFERIR_ARGUMENTO_INVALIDO );
        } catch (IllegalArgumentException e) {
            log.error("Argumento inválido na transferência: {}", e.getMessage());
            throw new BackendException(e.getMessage(), HttpStatus.BAD_REQUEST, BackEndExceptionEnum.ERRO_AO_TRANSFERIR_ARGUMENTO_INVALIDO);
        } catch (SaldoInsuficienteException e) {
            log.error("Saldo insuficiente na transferência: {}", e.getMessage());
            throw new BackendException(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY, BackEndExceptionEnum.ERRO_AO_TRANSFERIR_SALDO_INSUFICIENTE);
        } catch (ContaInativaException e) {
            log.error("Conta inativa na transferência: {}", e.getMessage());
            throw new BackendException(e.getMessage(), HttpStatus.FORBIDDEN, BackEndExceptionEnum.ERRO_AO_TRANSFERIR_CONTA_INATIVA);
        } catch (ContaNaoEncontradaException e) {
            log.error("Conta não encontrada na transferência: {}", e.getMessage());
            throw new BackendException(e.getMessage(), HttpStatus.NOT_FOUND, BackEndExceptionEnum.ERRO_AO_TRANSFERIR_CONTA_NAO_ENCONTRADA);
        } catch (OptimisticLockException e) {
            log.error("Conflito de versão na transferência: {}", e.getMessage());
            throw new BackendException(e.getMessage(), HttpStatus.CONFLICT, BackEndExceptionEnum.ERRO_AO_TRANSFERIR_CONFLITO_VERSAO);
        } catch (Exception e) {
            log.error("Erro ao realizar transferência: {}", e.getMessage());
            throw new BackendException("Erro ao realizar transferência", BackEndExceptionEnum.ERRO_AO_TRANSFERIR_VALOR_ENTRE_BENEFICIOS);
        }
    }

    /**
     * Valida objeto utilizando Bean Validation.
     *
     * @param <T> Tipo da objeto a ser validado.
     * @param objeto Objeto a ser validado.
     * @throws BackendException se houver violações de restrições.
     */
    private <T> void validarObjeto(T objeto) {
        Set<ConstraintViolation<T>> violations = validator.validate(objeto);
        if (!violations.isEmpty()) {
            String mensagens = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", "));
            throw new BackendException(mensagens, HttpStatus.BAD_REQUEST);
        }
    }

}
