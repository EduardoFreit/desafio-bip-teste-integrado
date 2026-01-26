package com.example.backend.mapper;

import org.springframework.stereotype.Component;

import com.example.backend.dto.BeneficioDTO;
import com.example.backend.mapper.interfaces.IBeneficioMapper;
import com.example.ejb.model.Beneficio;

/**
 *
 * Mapper para conversão entre Beneficio e BeneficioDTO
 * @author lepf9
 */
@Component
public class BeneficioMapper implements IBeneficioMapper {
    
    @Override
    public BeneficioDTO beneficioParaBeneficioDTO(Beneficio beneficio) {
        if (beneficio == null) {
            return null;
        }
        return new BeneficioDTO(
            beneficio.getId(),
            beneficio.getNome(),
            beneficio.getDescricao(),
            beneficio.getValor(),
            beneficio.getAtivo(),
            beneficio.getVersion()
        );
    }

    @Override
    public Beneficio beneficioDTOParaBeneficio(BeneficioDTO dto) {
        if (dto == null) {
            return null;
        }
        Beneficio beneficio = new Beneficio();
        beneficio.setId(dto.id());
        beneficio.setNome(dto.nome());
        beneficio.setDescricao(dto.descricao());
        beneficio.setValor(dto.valor());
        beneficio.setAtivo(dto.ativo());
        beneficio.setVersion(dto.version());
        return beneficio;
    }

    @Override
    public void beneficioDTOParaBeneficioAtualizar(BeneficioDTO dto, Beneficio beneficio) {
        beneficio.setNome(dto.nome());
        beneficio.setDescricao(dto.descricao());
        beneficio.setAtivo(dto.ativo());
    }


}
