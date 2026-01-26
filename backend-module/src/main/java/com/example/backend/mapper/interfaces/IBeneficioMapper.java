package com.example.backend.mapper.interfaces;

import com.example.backend.dto.BeneficioDTO;
import com.example.ejb.model.Beneficio;

/**
 *
 * Mapper para conversão entre Beneficio e BeneficioDTO
 * @author lepf9
 */
public interface IBeneficioMapper {
    BeneficioDTO beneficioParaBeneficioDTO(Beneficio beneficio);
    Beneficio beneficioDTOParaBeneficio(BeneficioDTO dto);
}
