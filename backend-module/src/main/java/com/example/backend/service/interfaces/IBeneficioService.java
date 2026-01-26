package com.example.backend.service.interfaces;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.backend.dto.BeneficioDto;
import com.example.backend.dto.TransferenciaRequest;

public interface IBeneficioService {

    public Page<BeneficioDto> listar(Pageable pageable, String nome, Boolean ativo);
    public BeneficioDto criar(BeneficioDto dto);
    public BeneficioDto buscarPorId(Long id);
    public BeneficioDto atualizar(Long id, BeneficioDto dto);
    public void deletar(Long id);
    public void transferir(TransferenciaRequest request);
}
