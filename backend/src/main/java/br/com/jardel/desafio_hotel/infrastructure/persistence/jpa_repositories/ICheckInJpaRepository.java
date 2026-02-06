/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package br.com.jardel.desafio_hotel.infrastructure.persistence.jpa_repositories;

import br.com.jardel.desafio_hotel.infrastructure.persistence.entities.CheckInEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.math.BigDecimal;

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
    where c.dataEntrada <= :agora and c.dataSaida > :agora
    order by c.idHospede asc, c.dataSaida desc
    """)
    List<CheckInEntity> listarPresentesOrdenado(LocalDateTime agora);

    @Query("""
    select c from CheckInEntity c
    where c.dataSaida <= :agora
    order by c.idHospede asc, c.dataSaida desc
    """)
    List<CheckInEntity> listarAusentesOrdenado(LocalDateTime agora);
    
    @Query(value = """
        select * from (
            select distinct on (guest_id) *
            from check_ins
            where entry_at <= :agora and exit_at > :agora
            order by guest_id, exit_at desc
        ) t
        order by t.exit_at desc
        limit :limit offset :offset
        """, nativeQuery = true)
    List<CheckInEntity> listarPresentesPaginado(@Param("agora") LocalDateTime agora,
                                                @Param("limit") int limit,
                                                @Param("offset") int offset);

    @Query(value = """
        select count(distinct guest_id)
        from check_ins
        where entry_at <= :agora and exit_at > :agora
        """, nativeQuery = true)
    long contarPresentes(@Param("agora") LocalDateTime agora);

    @Query(value = """
        select * from (
            select distinct on (guest_id) *
            from check_ins
            where exit_at <= :agora
            order by guest_id, exit_at desc
        ) t
        order by t.exit_at desc
        limit :limit offset :offset
        """, nativeQuery = true)
    List<CheckInEntity> listarAusentesPaginado(@Param("agora") LocalDateTime agora,
                                               @Param("limit") int limit,
                                               @Param("offset") int offset);

    @Query(value = """
        select count(distinct guest_id)
        from check_ins
        where exit_at <= :agora
        """, nativeQuery = true)
    long contarAusentes(@Param("agora") LocalDateTime agora);

    @Query("""
        select coalesce(sum(coalesce(c.valorTotal, 0)), 0)
        from CheckInEntity c
        where c.idHospede = :idHospede
    """)
    BigDecimal somarTotalPorHospede(@Param("idHospede") Long idHospede);
    
    @Query("""
        select (count(c) > 0) from CheckInEntity c
        where c.idHospede = :idHospede
          and c.dataEntrada < :novaSaida
          and c.dataSaida > :novaEntrada
    """)
    boolean existeSobreposicao(@Param("idHospede") Long idHospede,
                               @Param("novaEntrada") LocalDateTime novaEntrada,
                               @Param("novaSaida") LocalDateTime novaSaida);

    @Query("""
        select (count(c) > 0) from CheckInEntity c
        where c.idHospede = :idHospede
          and c.id <> :checkInId
          and c.dataEntrada < :novaSaida
          and c.dataSaida > :novaEntrada
    """)
    boolean existeSobreposicaoExcluindoId(@Param("idHospede") Long idHospede,
                                          @Param("checkInId") Long checkInId,
                                          @Param("novaEntrada") LocalDateTime novaEntrada,
                                          @Param("novaSaida") LocalDateTime novaSaida);
    
}
