/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package br.com.jardel.desafio_hotel.application.use_cases;

import br.com.jardel.desafio_hotel.application.dtos.GastoHospedeDTO;
import br.com.jardel.desafio_hotel.api.dtos.PagedResult;
import br.com.jardel.desafio_hotel.api.dtos.PaginacaoRequest;

/**
 *
 * @author jarde
 */

/**
 * Interface segregada para consulta de hóspedes ausentes.
 */
public interface IConsultarHospedesAusentesUseCase 
        extends IUseCase<PaginacaoRequest, PagedResult<GastoHospedeDTO>> {
    
}