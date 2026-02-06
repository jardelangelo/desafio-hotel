/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package br.com.jardel.desafio_hotel.application.use_cases;

import br.com.jardel.desafio_hotel.api.dtos.BuscarHospedePorIdRequest;
import br.com.jardel.desafio_hotel.domain.models.Hospede;

/**
 *
 * @author jarde
 */

/** Buscar hóspede por id. */
public interface IBuscarHospedePorIdUseCase extends IUseCase<BuscarHospedePorIdRequest, Hospede> {
    
}
