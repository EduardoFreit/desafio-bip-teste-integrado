package com.example.backend.service.interfaces;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.backend.dto.BeneficioDTO;
import com.example.backend.dto.TransferenciaRequest;

/**
 *
 * Serviço para operações relacionadas a Benefício
 * @author lepf9
 */
public interface IBeneficioService {

    public Page<BeneficioDTO> listar(Pageable pageable, String nome, Boolean ativo);
    public List<BeneficioDTO> listarTodos();
    public BeneficioDTO criar(BeneficioDTO dto);
    public BeneficioDTO buscarPorId(Long id);
    public BeneficioDTO atualizar(Long id, BeneficioDTO dto);
    public void deletar(Long id);
    public void transferir(TransferenciaRequest request);
}
