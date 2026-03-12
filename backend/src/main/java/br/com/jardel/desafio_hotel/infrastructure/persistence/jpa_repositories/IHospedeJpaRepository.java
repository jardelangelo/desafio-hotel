/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package br.com.jardel.desafio_hotel.infrastructure.persistence.jpa_repositories;

import br.com.jardel.desafio_hotel.infrastructure.persistence.entities.HospedeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author jarde
 */

public interface IHospedeJpaRepository extends JpaRepository<HospedeEntity, Long> {

    Optional<HospedeEntity> findByDocumento(String documento);

    boolean existsByDocumento(String documento);

    List<HospedeEntity> findByNomeContainingIgnoreCaseOrDocumentoContainingOrTelefoneContaining(
            String nome,
            String documento,
            String telefone
    );
}
