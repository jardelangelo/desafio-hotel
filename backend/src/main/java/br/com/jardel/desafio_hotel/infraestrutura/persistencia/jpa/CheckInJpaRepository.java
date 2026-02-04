/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package br.com.jardel.desafio_hotel.infraestrutura.persistencia.jpa;

import br.com.jardel.desafio_hotel.infraestrutura.persistencia.entidade.CheckInEntidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author jarde
 */
public interface CheckInJpaRepository extends JpaRepository<CheckInEntidade, Long> {

    List<CheckInEntidade> findByIdHospedeOrderByDataEntradaDesc(Long idHospede);

    @Query("""
        select c from CheckInEntidade c
        where c.dataEntrada <= :agora and c.dataSaida >= :agora
        order by c.dataEntrada desc
    """)
    List<CheckInEntidade> listarPresentes(LocalDateTime agora);

    @Query("""
        select c from CheckInEntidade c
        where c.dataSaida < :agora
        order by c.dataSaida desc
    """)
    List<CheckInEntidade> listarAusentes(LocalDateTime agora);
}
