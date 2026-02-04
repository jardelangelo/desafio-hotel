/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.jardel.desafio_hotel.aplicacao.caso_uso;

import br.com.jardel.desafio_hotel.aplicacao.dto.GastoHospedeDTO;
import br.com.jardel.desafio_hotel.dominio.modelo.CheckIn;
import br.com.jardel.desafio_hotel.dominio.modelo.Hospede;
import br.com.jardel.desafio_hotel.dominio.porta.CheckInRepositorio;
import br.com.jardel.desafio_hotel.dominio.porta.HospedeRepositorio;
import br.com.jardel.desafio_hotel.dominio.servico.CalculadoraHospedagem;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author jarde
 */
public class ConsultarHospedesPresentes {
    
    private final HospedeRepositorio hospedeRepositorio;
    private final CheckInRepositorio checkInRepositorio;
    private final CalculadoraHospedagem calculadora;
    
    public ConsultarHospedesPresentes(HospedeRepositorio hospedeRepositorio,
                                      CheckInRepositorio checkInRepositorio,
                                      CalculadoraHospedagem calculadora) {
        this.hospedeRepositorio = hospedeRepositorio;
        this.checkInRepositorio = checkInRepositorio;
        this.calculadora = calculadora;
    }
    
    public List<GastoHospedeDTO> executar() {
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
