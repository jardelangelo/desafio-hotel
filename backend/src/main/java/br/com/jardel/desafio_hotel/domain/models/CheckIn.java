/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.jardel.desafio_hotel.domain.models;

import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 *
 * @author jarde
 */

public record CheckIn(
        Long id,
        Long idHospede,
        LocalDateTime dataEntrada,
        LocalDateTime dataSaida,
        boolean adicionalVeiculo,
        BigDecimal valorTotal
) {}
