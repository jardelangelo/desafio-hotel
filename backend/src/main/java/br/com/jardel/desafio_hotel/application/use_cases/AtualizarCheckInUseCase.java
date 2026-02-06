/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.jardel.desafio_hotel.application.use_cases;

import br.com.jardel.desafio_hotel.api.exceptions.NotFoundException;
import br.com.jardel.desafio_hotel.api.dtos.AtualizarCheckInRequest;
import br.com.jardel.desafio_hotel.domain.models.CheckIn;
import br.com.jardel.desafio_hotel.domain.repositories.ICheckInRepository;
import br.com.jardel.desafio_hotel.domain.services.ICalculadoraHospedagemService;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 *
 * @author jarde
 */

public class AtualizarCheckInUseCase implements IAtualizarCheckInUseCase {

    private final ICheckInRepository checkInRepositorio;
    private final ICalculadoraHospedagemService calculadora;

    public AtualizarCheckInUseCase(ICheckInRepository checkInRepositorio,
                                   ICalculadoraHospedagemService calculadora) {
        this.checkInRepositorio = checkInRepositorio;
        this.calculadora = calculadora;
    }

    @Override
    public CheckIn execute(AtualizarCheckInRequest request) {
        if (request == null) throw new IllegalArgumentException("request obrigatorio");
        if (request.id() == null) throw new IllegalArgumentException("id obrigatorio");

        CheckIn atual = checkInRepositorio.buscarPorId(request.id())
                .orElseThrow(() -> new NotFoundException("checkin nao encontrado"));

        LocalDateTime novaEntrada = request.dataEntrada() != null ? request.dataEntrada() : atual.dataEntrada();
        LocalDateTime novaSaida   = request.dataSaida()   != null ? request.dataSaida()   : atual.dataSaida();
        boolean novoVeiculo       = request.adicionalVeiculo() != null ? request.adicionalVeiculo() : atual.adicionalVeiculo();

        if (novaSaida.isBefore(novaEntrada))
            throw new IllegalArgumentException("dataSaida deve ser maior/igual dataEntrada");
        
        if (checkInRepositorio.existeSobreposicaoExcluindoId(atual.idHospede(), atual.id(), novaEntrada, novaSaida)) {
            throw new br.com.jardel.desafio_hotel.api.exceptions.ConflictException(
                "Hospede ja possui outra hospedagem no periodo informado"
            );
        }
        
        CheckIn paraCalcular = new CheckIn(atual.id(), atual.idHospede(), novaEntrada, novaSaida, novoVeiculo, null);
        BigDecimal novoTotal = calculadora.calcularTotalHospedagem(paraCalcular);

        CheckIn atualizado = new CheckIn(atual.id(), atual.idHospede(), novaEntrada, novaSaida, novoVeiculo, novoTotal);
        return checkInRepositorio.salvar(atualizado);
    }
}
