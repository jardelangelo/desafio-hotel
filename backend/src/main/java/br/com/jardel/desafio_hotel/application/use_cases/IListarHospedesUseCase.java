/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package br.com.jardel.desafio_hotel.application.use_cases;

import br.com.jardel.desafio_hotel.api.dtos.EmptyRequest;
import br.com.jardel.desafio_hotel.domain.models.Hospede;
import java.util.List;


/**
 *
 * @author jarde
 */

/** Listar todos os hóspedes. */
public interface IListarHospedesUseCase extends IUseCase<EmptyRequest, List<Hospede>> {
    
}
