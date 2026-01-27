package com.example.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import com.example.backend.dto.BeneficioDTO;
import com.example.backend.dto.TransferenciaRequest;
import com.example.backend.enuns.BackEndExceptionEnum;
import com.example.backend.exception.BackendException;
import com.example.backend.repository.BeneficioRepository;
import com.example.backend.service.interfaces.IBeneficioService;
import com.example.ejb.model.Beneficio;

@SpringBootTest
@ActiveProfiles("test")
public class BeneficioServiceTest {

    @Autowired
    private IBeneficioService beneficioService;

    @Autowired
    private BeneficioRepository beneficioRepository;

    // TESTES DE SUCESSO
    @BeforeEach
    public void setupAntesTests() {
        beneficioRepository.deleteAll();

        Beneficio beneficio1 = new Beneficio();
        beneficio1.setNome("Beneficio A");
        beneficio1.setDescricao("Descricao A");
        beneficio1.setValor(new BigDecimal("1000.00"));
        beneficio1.setAtivo(true);
        beneficioRepository.save(beneficio1);

        Beneficio beneficio2 = new Beneficio();
        beneficio2.setNome("Beneficio B");
        beneficio2.setDescricao("Descricao B");
        beneficio2.setValor(new BigDecimal("500.00"));
        beneficio2.setAtivo(true);
        beneficioRepository.save(beneficio2);

        Beneficio beneficio3 = new Beneficio();
        beneficio3.setNome("Beneficio C");
        beneficio3.setDescricao("Descricao C");
        beneficio3.setValor(new BigDecimal("750.00"));
        beneficio3.setAtivo(false);
        beneficioRepository.save(beneficio3);
    }

    @Test
    @DisplayName("Deve listar todos beneficios")
    public void listarBeneficiosTest() {
        List<BeneficioDTO> beneficioDTOs = beneficioService.listarTodos();
        assertEquals(3, beneficioDTOs.size());
    }

    @Test
    @DisplayName("Deve listar beneficios com filtro de nome")
    public void listarBeneficiosComFiltroDeNomeTest() {
        Pageable pageable = Pageable.unpaged();
        Page<BeneficioDTO> beneficiosPage = beneficioService.listar(pageable, "Beneficio A", null);
        assertEquals(1, beneficiosPage.getTotalElements());
    }

    @Test
    @DisplayName("Deve listar beneficios com filtro de ativo")
    public void listarBeneficiosComFiltroDeAtivoTest() {
        Pageable pageable = Pageable.unpaged();
        Page<BeneficioDTO> beneficiosPage = beneficioService.listar(pageable, null, true);
        assertEquals(2, beneficiosPage.getTotalElements());     
    }

    @Test
    @DisplayName("Deve listar beneficios com filtro de nome e ativo")
    public void listarBeneficiosComFiltroDeNomeEAtivoTest() {
        Pageable pageable = Pageable.unpaged();
        Page<BeneficioDTO> beneficiosPage = beneficioService.listar(pageable, "Beneficio B", true);
        assertEquals(1, beneficiosPage.getTotalElements());     
    }

    @Test
    @DisplayName("Deve listar beneficios ordenado por nome desc")
    public void listarBeneficiosOrdenadoPorNomeDescTest() {
        Pageable pageable = PageRequest.of(
            0,
            10,
            Sort.by(Sort.Direction.DESC, "nome")
        );  
        Page<BeneficioDTO> beneficiosPage = beneficioService.listar(pageable, null, null);
        assertEquals(3, beneficiosPage.getTotalElements()); 
        assertEquals("Beneficio C", beneficiosPage.getContent().get(0).nome());
        assertEquals("Beneficio B", beneficiosPage.getContent().get(1).nome());
        assertEquals("Beneficio A", beneficiosPage.getContent().get(2).nome());
    }

    @Test
    @DisplayName("Deve listar beneficios ordenado por nome asc")
    public void listarBeneficiosOrdenadoPorNomeAscTest() {
        Pageable pageable = PageRequest.of(
            0,
            10,
            Sort.by(Sort.Direction.ASC, "nome")
        );  
        Page<BeneficioDTO> beneficiosPage = beneficioService.listar(pageable, null, null);
        assertEquals(3, beneficiosPage.getTotalElements()); 
        assertEquals("Beneficio A", beneficiosPage.getContent().get(0).nome());
        assertEquals("Beneficio B", beneficiosPage.getContent().get(1).nome());
        assertEquals("Beneficio C", beneficiosPage.getContent().get(2).nome());
    }

    @Test
    @DisplayName("Deve criar um novo beneficio")
    public void criarBeneficioTest() {
        BeneficioDTO novoBeneficio = new BeneficioDTO(null, "Beneficio D", "Descricao D", new BigDecimal("1200.00"), true, null);
        beneficioService.criar(novoBeneficio);
        Pageable pageable = Pageable.unpaged();
        Page<BeneficioDTO> beneficiosPage = beneficioService.listar(pageable, null, null);
        assertEquals(4, beneficiosPage.getTotalElements()); 
    }
    
    @Test
    @DisplayName("Deve buscar beneficio por ID")
    public void buscarBeneficioPorIdTest() {
        Pageable pageable = Pageable.unpaged();
        Page<BeneficioDTO> beneficiosPage = beneficioService.listar(pageable, null, true);
        BeneficioDTO primeiroBeneficio = beneficiosPage.getContent().get(0);
        BeneficioDTO beneficioDto = beneficioService.buscarPorId(primeiroBeneficio.id());
        assertEquals(primeiroBeneficio.id(), beneficioDto.id());
    }

    @Test
    @DisplayName("Deve atualizar um beneficio existente")
    public void atualizarBeneficioTest() {
        Pageable pageable = Pageable.unpaged();
        Page<BeneficioDTO> beneficiosPage = beneficioService.listar(pageable, null, true);
        BeneficioDTO primeiroBeneficio = beneficiosPage.getContent().get(0);
        
        String novoNome = "Beneficio A Atualizado";
        String novaDescricao = "Descricao A Atualizada";
        Boolean novoAtivo = false;

        BeneficioDTO beneficioAtualizado = new BeneficioDTO(primeiroBeneficio.id(), novoNome, novaDescricao, primeiroBeneficio.valor(), novoAtivo, primeiroBeneficio.version());
        BeneficioDTO beneficioDto = beneficioService.atualizar(primeiroBeneficio.id(), beneficioAtualizado);
        assertEquals(novoNome, beneficioDto.nome());
        assertEquals(novaDescricao, beneficioDto.descricao());
        assertEquals(novoAtivo, beneficioDto.ativo());
    }

    @Test
    @DisplayName("Deve deletar um beneficio existente")
    public void deletarBeneficioTest() {
        Pageable pageable = Pageable.unpaged();
        Page<BeneficioDTO> beneficiosPage = beneficioService.listar(pageable, null, null);
        assertEquals(3, beneficiosPage.getTotalElements()); 
        BeneficioDTO primeiroBeneficio = beneficiosPage.getContent().get(0);    
        beneficioService.deletar(primeiroBeneficio.id());
        Page<BeneficioDTO> beneficiosAposDelecao = beneficioService.listar(pageable, null, null);
        assertEquals(2, beneficiosAposDelecao.getTotalElements()); 
    }

    @Test
    @DisplayName("Deve transferir valor entre beneficios")
    public void transferirValorEntreBeneficiosTest() {
        Pageable pageable = Pageable.unpaged();
        Page<BeneficioDTO> beneficiosPage = beneficioService.listar(pageable, null, true);
        BeneficioDTO beneficioOrigem = beneficiosPage.getContent().get(0);
        BeneficioDTO beneficioDestino = beneficiosPage.getContent().get(1);
        BigDecimal valorTransferencia = new BigDecimal("200.00");
        beneficioService.transferir(new TransferenciaRequest(beneficioOrigem.id(), beneficioDestino.id(), valorTransferencia));
        BeneficioDTO beneficioOrigemAposTransferencia = beneficioService.buscarPorId(beneficioOrigem.id());
        BeneficioDTO beneficioDestinoAposTransferencia = beneficioService.buscarPorId(beneficioDestino.id());
        assertEquals(beneficioOrigem.valor().subtract(valorTransferencia), beneficioOrigemAposTransferencia.valor());
        assertEquals(beneficioDestino.valor().add(valorTransferencia), beneficioDestinoAposTransferencia.valor());
    }

    // TESTES DE FALHA
    @Test
    @DisplayName("Deve falhar ao buscar beneficio inexistente por ID")
    public void buscarBeneficioInexistentePorIdTest() {
        Long idInexistente = 999L;
        BackendException ex = assertThrows(
            BackendException.class,
            () -> beneficioService.buscarPorId(idInexistente)
        );

        assertEquals(
            BackEndExceptionEnum.BENEFICIO_NAO_ENCONTRADO,
            ex.getExEnum()
        );
    }

    @Test
    @DisplayName("Deve falhar ao atualizar beneficio inexistente")
    public void atualizarBeneficioInexistenteTest() {
        Long idInexistente = 999L;
        BeneficioDTO beneficioAtualizado = new BeneficioDTO(idInexistente, "Nome Inexistente", "Descricao Inexistente", new BigDecimal("0.00"), true, null);

        BackendException ex = assertThrows(
            BackendException.class,
            () -> beneficioService.atualizar(idInexistente, beneficioAtualizado)
        );

        assertEquals(
            BackEndExceptionEnum.BENEFICIO_NAO_ENCONTRADO,
            ex.getExEnum()
        );
    }

    @Test
    @DisplayName("Deve falhar ao deletar beneficio inexistente")
    public void deletarBeneficioInexistenteTest() {
        Long idInexistente = 999L;
        
        BackendException ex = assertThrows(
            BackendException.class,
            () -> beneficioService.deletar(idInexistente)
        );

        assertEquals(
            BackEndExceptionEnum.BENEFICIO_NAO_ENCONTRADO,
            ex.getExEnum()
        );
    }

    @Test
    @DisplayName("Deve falhar ao transferir valor entre beneficios com saldo insuficiente")
    public void transferirValorEntreBeneficiosComSaldoInsuficienteTest() {
        Pageable pageable = Pageable.unpaged();
        Page<BeneficioDTO> beneficiosPage = beneficioService.listar(pageable, null, true);
        BeneficioDTO beneficioOrigem = beneficiosPage.getContent().get(0);
        BeneficioDTO beneficioDestino = beneficiosPage.getContent().get(1);
        BigDecimal valorTransferencia = beneficioOrigem.valor().add(new BigDecimal("10000.00"));
        
        BackendException ex = assertThrows(
            BackendException.class,
            () -> beneficioService.transferir(new TransferenciaRequest(beneficioOrigem.id(), beneficioDestino.id(), valorTransferencia))
        );

        assertEquals(
            BackEndExceptionEnum.ERRO_AO_TRANSFERIR_SALDO_INSUFICIENTE,
            ex.getExEnum()
        );
    }

    @Test
    @DisplayName("Deve falhar ao transferir valor entre beneficios quando conta origem não existe")
    public void transferirValorEntreBeneficiosContaOrigemInexistenteTest() {
        Pageable pageable = Pageable.unpaged();
        Page<BeneficioDTO> beneficiosPage = beneficioService.listar(pageable, null, true);
        BeneficioDTO beneficioDestino = beneficiosPage.getContent().get(1);
        BigDecimal valorTransferencia = new BigDecimal("100.00");
       
        BackendException ex = assertThrows(
            BackendException.class,
            () -> beneficioService.transferir(new TransferenciaRequest(999L, beneficioDestino.id(), valorTransferencia))
        );

        assertEquals(
            BackEndExceptionEnum.ERRO_AO_TRANSFERIR_CONTA_NAO_ENCONTRADA,
            ex.getExEnum()
        );
    }

    @Test
    @DisplayName("Deve falhar ao transferir valor entre beneficios quando conta destino não existe")
    public void transferirValorEntreBeneficiosContaDestinoInexistenteTest() {
        Pageable pageable = Pageable.unpaged();
        Page<BeneficioDTO> beneficiosPage = beneficioService.listar(pageable, null, true);
        BeneficioDTO beneficioOrigem = beneficiosPage.getContent().get(0);
        BigDecimal valorTransferencia = new BigDecimal("100.00");

        BackendException ex = assertThrows(
            BackendException.class,
            () -> beneficioService.transferir(new TransferenciaRequest(beneficioOrigem.id(), 999L, valorTransferencia))
        );

        assertEquals(
            BackEndExceptionEnum.ERRO_AO_TRANSFERIR_CONTA_NAO_ENCONTRADA,
            ex.getExEnum()
        );
    }

    @Test
    @DisplayName("Deve falhar ao transferir valor entre beneficios com argumento inválido")
    public void transferirValorEntreBeneficiosComArgumentoInvalidoTest() {
        BigDecimal valorTransferencia = new BigDecimal("-100.00");
        
        BackendException ex = assertThrows(
            BackendException.class,
            () -> beneficioService.transferir(new TransferenciaRequest(1L, 1L, valorTransferencia))
        );

        assertEquals(
            BackEndExceptionEnum.ERRO_AO_TRANSFERIR_ARGUMENTO_INVALIDO,
            ex.getExEnum()
        );
    }

    @Test
    @DisplayName("Deve falhar ao transferir valor entre beneficios com conta inativa")
    public void transferirValorEntreBeneficiosComContaInativaTest() {
        Pageable pageable = Pageable.unpaged();
        Page<BeneficioDTO> beneficiosPage = beneficioService.listar(pageable, null, false);
        BeneficioDTO beneficioInativo = beneficiosPage.getContent().get(0);
        beneficiosPage = beneficioService.listar(pageable, null, true);
        BeneficioDTO beneficioAtivo = beneficiosPage.getContent().get(0);
        BigDecimal valorTransferencia = new BigDecimal("100.00");
        
        BackendException ex = assertThrows(
            BackendException.class,
            () -> beneficioService.transferir(new TransferenciaRequest(beneficioAtivo.id(), beneficioInativo.id(), valorTransferencia))
        );

        assertEquals(
            BackEndExceptionEnum.ERRO_AO_TRANSFERIR_CONTA_INATIVA,
            ex.getExEnum()
        );
    }

}
