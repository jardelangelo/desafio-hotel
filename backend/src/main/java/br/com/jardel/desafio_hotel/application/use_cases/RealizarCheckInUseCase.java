/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.jardel.desafio_hotel.application.use_cases;

import br.com.jardel.desafio_hotel.domain.models.CheckIn;
import br.com.jardel.desafio_hotel.api.exceptions.NotFoundException;

import java.time.LocalDateTime;
import br.com.jardel.desafio_hotel.domain.repositories.ICheckInRepository;
import br.com.jardel.desafio_hotel.domain.repositories.IHospedeRepository;

/**
 *
 * @author jarde
 */
public class RealizarCheckInUseCase implements IRealizarCheckInUseCase {
    
    private final IHospedeRepository hospedeRepositorio;
    private final ICheckInRepository checkInRepositorio;
    
    public RealizarCheckInUseCase(IHospedeRepository hospedeRepositorio, ICheckInRepository checkInRepositorio) {
        this.hospedeRepositorio = hospedeRepositorio;
        this.checkInRepositorio = checkInRepositorio;
    }
    
    @Override
    public CheckIn execute(Long idHospede, LocalDateTime dataEntrada, LocalDateTime dataSaida, boolean adicionalVeiculo) {
        if (idHospede == null) throw new IllegalArgumentException("idHospede obrigatorio");
        if (dataEntrada == null) throw new IllegalArgumentException("dataEntrada obrigatoria");
        if (dataSaida == null) throw new IllegalArgumentException("dataSaida obrigatoria");
        if (dataSaida.isBefore(dataEntrada)) throw new IllegalArgumentException("dataSaida deve ser maior/igual dataEntrada");

        hospedeRepositorio.buscarPorId(idHospede)
                .orElseThrow(() -> new NotFoundException("hospede nao encontrado"));

        CheckIn checkIn = new CheckIn(null, idHospede, dataEntrada, dataSaida, adicionalVeiculo);
        return checkInRepositorio.salvar(checkIn);
    }
    
}
