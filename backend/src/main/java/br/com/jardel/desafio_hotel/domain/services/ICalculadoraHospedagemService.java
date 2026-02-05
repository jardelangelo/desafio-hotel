/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package br.com.jardel.desafio_hotel.domain.services;

import br.com.jardel.desafio_hotel.domain.models.CheckIn;
import java.math.BigDecimal;

/**
 *
 * @author jarde
 */
public interface ICalculadoraHospedagemService {
    BigDecimal calcularTotalHospedagem(CheckIn checkIn);
}
