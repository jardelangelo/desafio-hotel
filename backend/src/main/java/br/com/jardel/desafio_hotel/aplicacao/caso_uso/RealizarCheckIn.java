/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.jardel.desafio_hotel.aplicacao.caso_uso;

import br.com.jardel.desafio_hotel.dominio.modelo.CheckIn;
import br.com.jardel.desafio_hotel.dominio.porta.CheckInRepositorio;
import br.com.jardel.desafio_hotel.dominio.porta.HospedeRepositorio;
import br.com.jardel.desafio_hotel.api.erro.NaoEncontradoException;

import java.time.LocalDateTime;

/**
 *
 * @author jarde
 */
public class RealizarCheckIn {
    
    private final HospedeRepositorio hospedeRepositorio;
    private final CheckInRepositorio checkInRepositorio;
    
    public RealizarCheckIn(HospedeRepositorio hospedeRepositorio, CheckInRepositorio checkInRepositorio) {
        this.hospedeRepositorio = hospedeRepositorio;
        this.checkInRepositorio = checkInRepositorio;
    }
    
    public CheckIn executar(Long idHospede, LocalDateTime dataEntrada, LocalDateTime dataSaida, boolean adicionalVeiculo) {
        if (idHospede == null) throw new IllegalArgumentException("idHospede obrigatorio");
        if (dataEntrada == null) throw new IllegalArgumentException("dataEntrada obrigatoria");
        if (dataSaida == null) throw new IllegalArgumentException("dataSaida obrigatoria");
        if (dataSaida.isBefore(dataEntrada)) throw new IllegalArgumentException("dataSaida deve ser maior/igual dataEntrada");

        hospedeRepositorio.buscarPorId(idHospede)
                .orElseThrow(() -> new NaoEncontradoException("hospede nao encontrado"));

        CheckIn checkIn = new CheckIn(null, idHospede, dataEntrada, dataSaida, adicionalVeiculo);
        return checkInRepositorio.salvar(checkIn);
    }
    
}
