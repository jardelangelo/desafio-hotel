/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.jardel.desafio_hotel.infraestrutura.config;

import br.com.jardel.desafio_hotel.aplicacao.caso_uso.*;
import br.com.jardel.desafio_hotel.dominio.porta.CheckInRepositorio;
import br.com.jardel.desafio_hotel.dominio.porta.HospedeRepositorio;
import br.com.jardel.desafio_hotel.dominio.servico.CalculadoraHospedagem;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author jarde
 */

@Configuration
public class CasosDeUsoConfig {
    
    @Bean
    public CalculadoraHospedagem calculadoraHospedagem() {
        return new CalculadoraHospedagem();
    }

    @Bean
    public CadastrarHospede cadastrarHospede(HospedeRepositorio hospedeRepositorio) {
        return new CadastrarHospede(hospedeRepositorio);
    }

    @Bean
    public RealizarCheckIn realizarCheckIn(HospedeRepositorio hospedeRepositorio,
                                           CheckInRepositorio checkInRepositorio) {
        return new RealizarCheckIn(hospedeRepositorio, checkInRepositorio);
    }

    @Bean
    public ConsultarHospedesPresentes consultarHospedesPresentes(HospedeRepositorio hospedeRepositorio,
                                                                 CheckInRepositorio checkInRepositorio,
                                                                 CalculadoraHospedagem calculadora) {
        return new ConsultarHospedesPresentes(hospedeRepositorio, checkInRepositorio, calculadora);
    }

    @Bean
    public ConsultarHospedesAusentes consultarHospedesAusentes(HospedeRepositorio hospedeRepositorio,
                                                               CheckInRepositorio checkInRepositorio,
                                                               CalculadoraHospedagem calculadora) {
        return new ConsultarHospedesAusentes(hospedeRepositorio, checkInRepositorio, calculadora);
    }
    
}
