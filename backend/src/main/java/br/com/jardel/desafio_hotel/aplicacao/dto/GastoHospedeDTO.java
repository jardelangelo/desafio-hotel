/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.jardel.desafio_hotel.aplicacao.dto;

import java.math.BigDecimal;

/**
 *
 * @author jarde
 */
public record GastoHospedeDTO(
        Long idHospede,
        String nome,
        String documento,
        String telefone,
        BigDecimal valorTotalGasto,
        BigDecimal valorUltimaHospedagem
) {}
