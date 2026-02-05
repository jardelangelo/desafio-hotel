/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.jardel.desafio_hotel.application.use_cases;

import br.com.jardel.desafio_hotel.application.dtos.GastoHospedeDTO;
import br.com.jardel.desafio_hotel.domain.models.CheckIn;
import br.com.jardel.desafio_hotel.domain.models.Hospede;
import br.com.jardel.desafio_hotel.domain.services.ICalculadoraHospedagemService;

import java.math.BigDecimal;
import java.util.List;
import br.com.jardel.desafio_hotel.domain.repositories.ICheckInRepository;
import br.com.jardel.desafio_hotel.domain.repositories.IHospedeRepository;

/**
 *
 * @author jarde
 */
public class ConsultarHospedesPresentesUseCase implements IConsultarHospedesPresentesUseCase {
    
    private final IHospedeRepository hospedeRepositorio;
    private final ICheckInRepository checkInRepositorio;
    private final ICalculadoraHospedagemService calculadora;
    
    public ConsultarHospedesPresentesUseCase(IHospedeRepository hospedeRepositorio,
                                      ICheckInRepository checkInRepositorio,
                                      ICalculadoraHospedagemService calculadora) {
        this.hospedeRepositorio = hospedeRepositorio;
        this.checkInRepositorio = checkInRepositorio;
        this.calculadora = calculadora;
    }
    
    @Override
    public List<GastoHospedeDTO> execute() {
        return checkInRepositorio.listarPresentes().stream()
                .map(this::montarGastoHospede)
                .toList();
    }
    
    private GastoHospedeDTO montarGastoHospede(CheckIn checkInAtual) {
        Hospede hospede = hospedeRepositorio.buscarPorId(checkInAtual.idHospede())
                .orElseThrow(() -> new IllegalStateException("Hospede do check-in nao encontrado"));

        List<CheckIn> historico = checkInRepositorio.listarPorHospede(hospede.id());

        BigDecimal totalGasto = historico.stream()
                .map(calculadora::calcularTotalHospedagem)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal ultimaHospedagem = calculadora.calcularTotalHospedagem(checkInAtual);

        return new GastoHospedeDTO(
                hospede.id(),
                hospede.nome(),
                hospede.documento(),
                hospede.telefone(),
                totalGasto,
                ultimaHospedagem
        );
    }
    
}
