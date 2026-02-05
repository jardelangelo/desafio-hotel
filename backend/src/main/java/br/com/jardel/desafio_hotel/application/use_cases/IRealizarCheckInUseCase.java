/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package br.com.jardel.desafio_hotel.application.use_cases;

import br.com.jardel.desafio_hotel.domain.models.CheckIn;
import java.time.LocalDateTime;

/**
 *
 * @author jarde
 */
public interface IRealizarCheckInUseCase {
    CheckIn execute(Long idHospede, LocalDateTime dataEntrada, LocalDateTime dataSaida, boolean adicionalVeiculo);
}
