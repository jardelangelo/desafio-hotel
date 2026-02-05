/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package br.com.jardel.desafio_hotel.infrastructure.persistence.jpa_repositories;

import br.com.jardel.desafio_hotel.infrastructure.persistence.entities.CheckInEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author jarde
 */
public interface ICheckInJpaRepository extends JpaRepository<CheckInEntity, Long> {

    List<CheckInEntity> findByIdHospedeOrderByDataEntradaDesc(Long idHospede);

    @Query("""
        select c from CheckInEntity c
        where c.dataEntrada <= :agora and c.dataSaida >= :agora
        order by c.dataEntrada desc
    """)
    List<CheckInEntity> listarPresentes(LocalDateTime agora);

    @Query("""
        select c from CheckInEntity c
        where c.dataSaida < :agora
        order by c.dataSaida desc
    """)
    List<CheckInEntity> listarAusentes(LocalDateTime agora);
 
    @Query("""
    select c from CheckInEntity c
    where c.dataSaida > :agora
    order by c.idHospede asc, c.dataSaida desc
    """)
    List<CheckInEntity> listarPresentesOrdenado(LocalDateTime agora);

    @Query("""
    select c from CheckInEntity c
    where c.dataSaida <= :agora
    order by c.idHospede asc, c.dataSaida desc
    """)
    List<CheckInEntity> listarAusentesOrdenado(LocalDateTime agora);
    
}
