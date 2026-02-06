/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package br.com.jardel.desafio_hotel.domain.repositories;

import br.com.jardel.desafio_hotel.domain.models.CheckIn;

import java.util.Optional;
import java.util.List;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 *
 * @author jarde
 */
public interface ICheckInRepository {
    
    CheckIn salvar(CheckIn checkIn);

    Optional<CheckIn> buscarPorId(Long id);
    
    boolean existeSobreposicao(Long idHospede, LocalDateTime novaEntrada, LocalDateTime novaSaida);
    boolean existeSobreposicaoExcluindoId(Long idHospede, Long checkInId, LocalDateTime novaEntrada, LocalDateTime novaSaida);
    
    // Consultas
    List<CheckIn> listarPresentes();
    List<CheckIn> listarAusentes();
    List<CheckIn> listarPorHospede(Long idHospede);
    
    List<CheckIn> listarPresentesPaginado(int page, int size);
    long contarPresentes();

    List<CheckIn> listarAusentesPaginado(int page, int size);
    long contarAusentes();

    BigDecimal somarTotalPorHospede(Long idHospede);
}
