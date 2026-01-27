package com.example.backend.controller;

import java.util.List;

import org.slf4j.MDC;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.backend.dto.BeneficioDTO;
import com.example.backend.dto.TransferenciaRequest;
import com.example.backend.exception.BackendException;
import com.example.backend.service.interfaces.IBeneficioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * Controller para gerenciamento de benefícios
 * 
 */
@RestController
@RequestMapping("/api/v1/beneficios")
@Slf4j
@Tag(name = "Gerenciamento de Benefícios", description = "Métodos para gerenciar benefícios e transferências de valores entre eles")
public class BeneficioController {

    private final IBeneficioService beneficioService;

    public BeneficioController(IBeneficioService beneficioService) {
        this.beneficioService = beneficioService;
    }

    @GetMapping(value = "/listar", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Listar benefícios", description = "Retorna uma lista com os benefícios cadastrados no sistema.")
    public ResponseEntity<Page<BeneficioDTO>> listar(
        @RequestParam(required = false, name="nome") String nome,
        @RequestParam(required = false, name="ativo") Boolean ativo,
        @ParameterObject
        @PageableDefault(
            size = 5,
            page = 0,
            sort = {"nome"},
            direction = Sort.Direction.ASC
        ) Pageable pageable) {
        
        log.info("Listando benefícios com nome: {} e ativo: {}", nome, ativo);

        Page<BeneficioDTO> page = beneficioService.listar(pageable, nome, ativo);

        log.info("Benefícios listados com sucesso, total de elementos: {}", page.getTotalElements());

        return ResponseEntity.ok(page);
    }

    @GetMapping("/listar-todos")
    @Operation(summary = "Listar todos os benefícios", description = "Retorna uma lista com todos os benefícios cadastrados no sistema.")
    public ResponseEntity<List<BeneficioDTO>> listarTodos() {
        
        log.info("Listando todos os benefícios");

        List<BeneficioDTO> beneficios = beneficioService.listarTodos();
        
        log.info("Todos os benefícios listados com sucesso");

        return ResponseEntity.ok(beneficios);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Criar benefício", description = "Cria um novo benefício no sistema.")
    public ResponseEntity<BeneficioDTO> criar(@RequestBody BeneficioDTO dto, UriComponentsBuilder uriBuilder) {
        
        log.info("Criando benefício: {}", dto);
        
        BeneficioDTO created = beneficioService.criar(dto);
        var uri = uriBuilder.path("/api/v1/beneficios/{id}").buildAndExpand(created.id()).toUri();
        
        log.info("Benefício criado com sucesso: {}", created);
        
        return ResponseEntity.created(uri).body(created);
    }

    @GetMapping(path="/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Buscar benefício por ID", description = "Retorna os dados de um benefício específico pelo seu ID.")
    public ResponseEntity<BeneficioDTO> buscarPorId(@NotNull @PathVariable("id") Long id) {
        
        log.info("Buscando benefício por ID: {}", id);
        
        BeneficioDTO dto = beneficioService.buscarPorId(id);

        log.info("Benefício encontrado: {}", dto);

        return ResponseEntity.ok(dto);
    }

    @PutMapping(path="/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Atualizar benefício", description = "Atualiza os dados de um benefício existente.")
    public ResponseEntity<BeneficioDTO> atualizar(@NotNull @PathVariable("id") Long id, @Valid @RequestBody BeneficioDTO dto) {
        
        log.info("Atualizando benefício ID: {} com dados: {}", id, dto);
        
        BeneficioDTO updated = beneficioService.atualizar(id, dto);

        log.info("Benefício atualizado com sucesso: {}", updated);

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping(path="/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Deletar benefício", description = "Deleta um benefício existente pelo seu ID.")
    public ResponseEntity<Void> deletar(@NotNull @PathVariable("id") Long id) {

        log.info("Deletando benefício por ID: {}", id);

        beneficioService.deletar(id);

        log.info("Benefício deletado com sucesso, ID: {}", id);

        return ResponseEntity.noContent().build();
    }

    @PostMapping(path="/transferir", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Transferir valor entre benefícios", description = "Realiza a transferência de valor entre dois benefícios.")
    public ResponseEntity<Void> transferir(@RequestBody TransferenciaRequest request) {
        try {

            MDC.put("tag", "TRANSFERENCIA");

            log.info("Iniciando transferência entre benefícios: {}", request);

            beneficioService.transferir(request);

            log.info("Transferência realizada com sucesso entre benefícios: {}", request);

            return ResponseEntity.ok().build();
        } catch (BackendException e) {
            throw e;
        } finally {
            MDC.remove("tag");
        }
    }
}
