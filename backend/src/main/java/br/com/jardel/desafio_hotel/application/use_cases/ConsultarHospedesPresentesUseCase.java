/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.jardel.desafio_hotel.application.use_cases;

import br.com.jardel.desafio_hotel.application.dtos.GastoHospedeDTO;
import br.com.jardel.desafio_hotel.domain.models.CheckIn;
import br.com.jardel.desafio_hotel.domain.models.Hospede;
import br.com.jardel.desafio_hotel.domain.services.ICalculadoraHospedagemService;
import br.com.jardel.desafio_hotel.domain.repositories.ICheckInRepository;
import br.com.jardel.desafio_hotel.domain.repositories.IHospedeRepository;

import java.math.BigDecimal;
import java.util.List;

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
    public br.com.jardel.desafio_hotel.api.dtos.PagedResult<GastoHospedeDTO> execute(
            br.com.jardel.desafio_hotel.api.dtos.PaginacaoRequest request
    ) {
        int page = request.page();
        int size = request.size();

        long total = checkInRepositorio.contarPresentes();
        var checkins = checkInRepositorio.listarPresentesPaginado(page, size);

        var items = checkins.stream().map(this::montarGastoHospede).toList();
        return br.com.jardel.desafio_hotel.api.dtos.PagedResult.of(items, page, size, total);
    }

    private GastoHospedeDTO montarGastoHospede(CheckIn checkInAtual) {
        Hospede hospede = hospedeRepositorio.buscarPorId(checkInAtual.idHospede())
                .orElseThrow(() -> new IllegalStateException("Hospede do check-in nao encontrado"));

        List<CheckIn> historico = checkInRepositorio.listarPorHospede(hospede.id());

        BigDecimal totalGasto = historico.stream()
                .map(this::valorTotalSeguro)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal ultimaHospedagem = valorTotalSeguro(checkInAtual);

        return new GastoHospedeDTO(
                hospede.id(),
                hospede.nome(),
                hospede.documento(),
                hospede.telefone(),
                checkInAtual.dataEntrada(),
                checkInAtual.dataSaida(),
                checkInAtual.adicionalVeiculo(),
                totalGasto,
                ultimaHospedagem
        );
    }

    private BigDecimal valorTotalSeguro(CheckIn c) {
        if (c.valorTotal() != null) return c.valorTotal();
        return calculadora.calcularTotalHospedagem(
                new CheckIn(c.id(), c.idHospede(), c.dataEntrada(), c.dataSaida(), c.adicionalVeiculo(), null)
        );
    }
    
}
