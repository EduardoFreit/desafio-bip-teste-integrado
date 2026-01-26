package com.example.backend.specification;

import org.springframework.data.jpa.domain.Specification;

import com.example.ejb.model.Beneficio;

/**
 *
 * @author lepf9
 */
public class BeneficioSpec {
    public static Specification<Beneficio> nomeLike(String nome) {
        return (root, query, cb) -> 
            nome == null ? null : cb.like(cb.lower(root.get("nome")), "%" + nome.toLowerCase() + "%");
    }

    public static Specification<Beneficio> ativoIgual(Boolean ativo) {
        return (root, query, cb) -> ativo == null ? null : cb.equal(root.get("ativo"), ativo);
    }
}
