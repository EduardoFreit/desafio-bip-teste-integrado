package com.example.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.backend.dto.BeneficioDto;
import com.example.backend.repository.BeneficioRepository;
import com.example.backend.service.interfaces.IBeneficioService;

public class BeneficioService implements IBeneficioService {


    @Autowired
    private BeneficioRepository beneficioRepository;

    @Override
    public List<BeneficioDto> listarBeneficios() {
        // Implementação do método para listar benefícios
        return beneficioRepository.findAll()
                .stream()
                .map(beneficio -> new BeneficioDto(beneficio.getNome()))
                .toList();
    }
}
