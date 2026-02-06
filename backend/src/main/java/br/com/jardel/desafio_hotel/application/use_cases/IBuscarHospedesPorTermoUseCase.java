/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package br.com.jardel.desafio_hotel.application.use_cases;

import br.com.jardel.desafio_hotel.api.dtos.BuscarHospedesPorTermoRequest;
import br.com.jardel.desafio_hotel.domain.models.Hospede;
import java.util.List;

/**
 *
 * @author jarde
 */

/** Buscar hóspedes por termo (nome/documento/telefone). */
public interface IBuscarHospedesPorTermoUseCase extends IUseCase<BuscarHospedesPorTermoRequest, List<Hospede>> {
    
}