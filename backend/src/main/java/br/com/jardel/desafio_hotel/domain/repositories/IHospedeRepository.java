/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package br.com.jardel.desafio_hotel.domain.repositories;

import br.com.jardel.desafio_hotel.domain.models.Hospede;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author jarde
 */
public interface IHospedeRepository {
    
    Hospede salvar(Hospede hospede);
    Optional<Hospede> buscarPorId(Long id);
    Optional<Hospede> buscarPorDocumento(String documento);
    List<Hospede> listarTodos();
    void excluirPorId(Long id);

    // Para busca no check-in: nome/documento/telefone
    List<Hospede> buscarPorTermo(String termo);
}
