/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.jardel.desafio_hotel.application.use_cases;

import br.com.jardel.desafio_hotel.domain.models.CheckIn;
import br.com.jardel.desafio_hotel.api.exceptions.NotFoundException;
import br.com.jardel.desafio_hotel.api.dtos.RealizarCheckInRequest;
import br.com.jardel.desafio_hotel.domain.repositories.ICheckInRepository;
import br.com.jardel.desafio_hotel.domain.repositories.IHospedeRepository;
import br.com.jardel.desafio_hotel.domain.services.ICalculadoraHospedagemService;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 *
 * @author jarde
 */

public class RealizarCheckInUseCase implements IRealizarCheckInUseCase {
    
    private final IHospedeRepository hospedeRepositorio;
    private final ICheckInRepository checkInRepositorio;
    private final ICalculadoraHospedagemService calculadora;

    public RealizarCheckInUseCase(IHospedeRepository hospedeRepositorio,
                                  ICheckInRepository checkInRepositorio,
                                  ICalculadoraHospedagemService calculadora) {
        this.hospedeRepositorio = hospedeRepositorio;
        this.checkInRepositorio = checkInRepositorio;
        this.calculadora = calculadora;
    }

    @Override
    public CheckIn execute(RealizarCheckInRequest request) {
        if (request == null) throw new IllegalArgumentException("Requisição inválida: corpo da requisição (request) é obrigatório.");
        if (request.idHospede() == null) throw new IllegalArgumentException("Hóspede é obrigatório.");
        if (request.dataEntrada() == null) throw new IllegalArgumentException("Data de entrada para CheckIn é obrigatória.");
        if (request.dataSaida() == null) throw new IllegalArgumentException("Data de saída para CheckIn é obrigatória.");
        if (request.dataSaida().isBefore(request.dataEntrada()))
            throw new IllegalArgumentException("A data de saída do check-in deve ser maior ou igual à data de entrada.");
        if (request.dataEntrada().isAfter(LocalDateTime.now()))
            throw new IllegalArgumentException("Não é permitido agendar check-in futuro. Por isso, a data de entrada deve ser hoje ou uma data anterior.");
        
        hospedeRepositorio.buscarPorId(request.idHospede())
                .orElseThrow(() -> new NotFoundException("Hóspede não encontrado."));

        // calcula e persiste snapshot
        CheckIn paraCalcular = new CheckIn(null, request.idHospede(), request.dataEntrada(), request.dataSaida(),
                request.adicionalVeiculo(), null);

        BigDecimal valorTotal = calculadora.calcularTotalHospedagem(paraCalcular);

        CheckIn novo = new CheckIn(null, request.idHospede(), request.dataEntrada(), request.dataSaida(),
                request.adicionalVeiculo(), valorTotal);

        if (checkInRepositorio.existeSobreposicao(request.idHospede(), request.dataEntrada(), request.dataSaida())) {
            throw new br.com.jardel.desafio_hotel.api.exceptions.ConflictException(
                "O hóspede já possui uma hospedagem no período informado."
            );
        }
        
        return checkInRepositorio.salvar(novo);
    }
    
}
