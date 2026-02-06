/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package br.com.jardel.desafio_hotel.application.use_cases;

import br.com.jardel.desafio_hotel.api.dtos.AtualizarHospedeRequest;
import br.com.jardel.desafio_hotel.domain.models.Hospede;


/**
 *
 * @author jarde
 */

/** Atualizar dados do hóspede (nome/telefone). */
public interface IAtualizarHospedeUseCase extends IUseCase<AtualizarHospedeRequest, Hospede> {
    
}
