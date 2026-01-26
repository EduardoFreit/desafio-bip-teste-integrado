package com.example.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.example.ejb.model.Beneficio;

/**
 *
 * Repositório para operações CRUD e consultas de Beneficio
 * @author lepf9
 */
public interface BeneficioRepository extends JpaRepository<Beneficio, Long> , JpaSpecificationExecutor<Beneficio> {}
