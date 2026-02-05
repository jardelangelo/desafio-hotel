/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.jardel.desafio_hotel.infrastructure.configs;

import br.com.jardel.desafio_hotel.application.use_cases.*;
import br.com.jardel.desafio_hotel.domain.services.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import br.com.jardel.desafio_hotel.domain.repositories.ICheckInRepository;
import br.com.jardel.desafio_hotel.domain.repositories.IHospedeRepository;

/**
 *
 * @author jarde
 */

@Configuration
public class CasosDeUsoConfig {
    
    @Bean
    public ICalculadoraHospedagemService calculadoraHospedagemService() {
        return new CalculadoraHospedagemService();
    }

    @Bean
    public ICadastrarHospedeUseCase cadastrarHospede(IHospedeRepository hospedeRepositorio) {
        return new CadastrarHospedeUseCase(hospedeRepositorio);
    }

    @Bean
    public IRealizarCheckInUseCase realizarCheckIn(IHospedeRepository hospedeRepositorio,
                                           ICheckInRepository checkInRepositorio) {
        return new RealizarCheckInUseCase(hospedeRepositorio, checkInRepositorio);
    }

    @Bean
    public IConsultarHospedesPresentesUseCase consultarHospedesPresentes(IHospedeRepository hospedeRepositorio,
                                                                 ICheckInRepository checkInRepositorio,
                                                                 ICalculadoraHospedagemService calculadora) {
        return new ConsultarHospedesPresentesUseCase(hospedeRepositorio, checkInRepositorio, calculadora);
    }

    @Bean
    public IConsultarHospedesAusentesUseCase consultarHospedesAusentes(IHospedeRepository hospedeRepositorio,
                                                               ICheckInRepository checkInRepositorio,
                                                               ICalculadoraHospedagemService calculadora) {
        return new ConsultarHospedesAusentesUseCase(hospedeRepositorio, checkInRepositorio, calculadora);
    }
    
}
