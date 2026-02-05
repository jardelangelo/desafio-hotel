/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package br.com.jardel.desafio_hotel.domain.repositories;

import br.com.jardel.desafio_hotel.domain.models.CheckIn;

import java.util.List;

/**
 *
 * @author jarde
 */
public interface ICheckInRepository {
    
    CheckIn salvar(CheckIn checkIn);

    // Consultas
    List<CheckIn> listarPresentes();
    List<CheckIn> listarAusentes();
    List<CheckIn> listarPorHospede(Long idHospede);
}
