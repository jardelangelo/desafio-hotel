/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package br.com.jardel.desafio_hotel.infrastructure.persistence.jpa_repositories;

import br.com.jardel.desafio_hotel.infrastructure.persistence.entities.HospedeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author jarde
 */

public interface IHospedeJpaRepository extends JpaRepository<HospedeEntity, Long> {

    Optional<HospedeEntity> findByDocumento(String documento);

    @Query("""
        select h from HospedeEntity h
        where lower(h.nome) like lower(concat('%', :termo, '%'))
           or h.documento like concat('%', :termo, '%')
           or h.telefone like concat('%', :termo, '%')
    """)
    List<HospedeEntity> buscarPorTermo(String termo);
}
