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
        if (request == null) throw new IllegalArgumentException("request obrigatorio");
        if (request.idHospede() == null) throw new IllegalArgumentException("idHospede obrigatorio");
        if (request.dataEntrada() == null) throw new IllegalArgumentException("dataEntrada obrigatoria");
        if (request.dataSaida() == null) throw new IllegalArgumentException("dataSaida obrigatoria");
        if (request.dataSaida().isBefore(request.dataEntrada()))
            throw new IllegalArgumentException("dataSaida deve ser maior/igual dataEntrada");

        hospedeRepositorio.buscarPorId(request.idHospede())
                .orElseThrow(() -> new NotFoundException("hospede nao encontrado"));

        // calcula e persiste snapshot
        CheckIn paraCalcular = new CheckIn(null, request.idHospede(), request.dataEntrada(), request.dataSaida(),
                request.adicionalVeiculo(), null);

        BigDecimal valorTotal = calculadora.calcularTotalHospedagem(paraCalcular);

        CheckIn novo = new CheckIn(null, request.idHospede(), request.dataEntrada(), request.dataSaida(),
                request.adicionalVeiculo(), valorTotal);

        if (checkInRepositorio.existeSobreposicao(request.idHospede(), request.dataEntrada(), request.dataSaida())) {
            throw new br.com.jardel.desafio_hotel.api.exceptions.ConflictException(
                "Hospede ja possui uma hospedagem no periodo informado"
            );
        }
        
        return checkInRepositorio.salvar(novo);
    }
    
}
