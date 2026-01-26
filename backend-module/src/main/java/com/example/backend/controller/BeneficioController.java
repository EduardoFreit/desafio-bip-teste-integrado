package com.example.backend.controller;

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
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.backend.dto.BeneficioDto;
import com.example.backend.dto.TransferenciaRequest;
import com.example.backend.service.interfaces.IBeneficioService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/beneficios")
@Slf4j
public class BeneficioController {

    private final IBeneficioService beneficioService;

    public BeneficioController(IBeneficioService beneficioService) {
        this.beneficioService = beneficioService;
    }

    @GetMapping("/listar")
    public ResponseEntity<Page<BeneficioDto>> listar(
        @RequestParam(required = false, name="nome") String nome,
        @RequestParam(required = false, name="ativo") Boolean ativo,
        @PageableDefault(
            size = 5,
            page = 0,
            sort = {"nome"},
            direction = Sort.Direction.ASC
        ) Pageable pageable) {
        
        log.info("Listando benefícios com nome: {} e ativo: {}", nome, ativo);

        Page<BeneficioDto> page = beneficioService.listar(pageable, nome, ativo);

        log.info("Benefícios listados com sucesso, total de elementos: {}", page.getTotalElements());

        return ResponseEntity.ok(page);
    }

    @PostMapping
    public ResponseEntity<BeneficioDto> criar(@RequestBody BeneficioDto dto, UriComponentsBuilder uriBuilder) {
        
        log.info("Criando benefício: {}", dto);
        
        BeneficioDto created = beneficioService.criar(dto);
        var uri = uriBuilder.path("/api/v1/beneficios/{id}").buildAndExpand(created.id()).toUri();
        
        log.info("Benefício criado com sucesso: {}", created);
        
        return ResponseEntity.created(uri).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BeneficioDto> buscarPorId(@NotNull @PathVariable("id") Long id) {
        
        log.info("Buscando benefício por ID: {}", id);
        
        BeneficioDto dto = beneficioService.buscarPorId(id);

        log.info("Benefício encontrado: {}", dto);

        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BeneficioDto> atualizar(@NotNull @PathVariable("id") Long id, @Valid @RequestBody BeneficioDto dto) {
        
        log.info("Atualizando benefício ID: {} com dados: {}", id, dto);
        
        BeneficioDto updated = beneficioService.atualizar(id, dto);

        log.info("Benefício atualizado com sucesso: {}", updated);

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@NotNull @PathVariable("id") Long id) {

        log.info("Deletando benefício por ID: {}", id);

        beneficioService.deletar(id);

        log.info("Benefício deletado com sucesso, ID: {}", id);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/transferir")
    public ResponseEntity<Void> transferir(@RequestBody TransferenciaRequest request) {

        log.info("Iniciando transferência entre benefícios: {}", request);

        beneficioService.transferir(request);

        log.info("Transferência realizada com sucesso entre benefícios: {}", request);

        return ResponseEntity.ok().build();
    }
}
