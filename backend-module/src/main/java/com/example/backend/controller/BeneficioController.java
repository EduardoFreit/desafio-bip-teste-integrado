package com.example.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.dto.BeneficioDto;
import com.example.backend.service.interfaces.IBeneficioService;

@RestController
@RequestMapping("/api/v1/beneficios")
public class BeneficioController {

    @Autowired
    private IBeneficioService beneficioService;

    @GetMapping
    public List<BeneficioDto> list() {
        return beneficioService.listarBeneficios();
    }
}
