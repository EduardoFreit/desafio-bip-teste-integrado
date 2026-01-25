package com.example.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ejb.model.Beneficio;

public interface BeneficioRepository extends JpaRepository<Beneficio, Long> {
    
}
