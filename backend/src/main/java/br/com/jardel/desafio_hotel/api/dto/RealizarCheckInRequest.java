/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.jardel.desafio_hotel.api.dto;

import java.time.LocalDateTime;

/**
 *
 * @author jarde
 */
public record RealizarCheckInRequest(
        Long idHospede,
        LocalDateTime dataEntrada,
        LocalDateTime dataSaida,
        boolean adicionalVeiculo
) {}
