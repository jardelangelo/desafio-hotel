/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package br.com.jardel.desafio_hotel.application.use_cases;

import br.com.jardel.desafio_hotel.application.dtos.GastoHospedeDTO;
import java.util.List;

/**
 *
 * @author jarde
 */
public interface IConsultarHospedesPresentesUseCase {
    List<GastoHospedeDTO> execute();
}
