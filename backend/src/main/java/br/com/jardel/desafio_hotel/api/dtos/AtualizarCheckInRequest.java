/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.jardel.desafio_hotel.api.dtos;

import java.time.LocalDateTime;

/**
 *
 * @author jarde
 */

public record AtualizarCheckInRequest(
        Long id,
        LocalDateTime dataEntrada,
        LocalDateTime dataSaida,
        Boolean adicionalVeiculo
) {}
