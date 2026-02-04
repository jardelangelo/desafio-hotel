/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.jardel.desafio_hotel.api.controller;

import br.com.jardel.desafio_hotel.api.dto.RealizarCheckInRequest;
import br.com.jardel.desafio_hotel.aplicacao.caso_uso.ConsultarHospedesAusentes;
import br.com.jardel.desafio_hotel.aplicacao.caso_uso.ConsultarHospedesPresentes;
import br.com.jardel.desafio_hotel.aplicacao.caso_uso.RealizarCheckIn;
import br.com.jardel.desafio_hotel.aplicacao.dto.GastoHospedeDTO;
import br.com.jardel.desafio_hotel.dominio.modelo.CheckIn;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *
 * @author jarde
 */

@RestController
@RequestMapping("/api/checkins")
public class CheckInController {
    
    private final RealizarCheckIn realizarCheckIn;
    private final ConsultarHospedesPresentes consultarPresentes;
    private final ConsultarHospedesAusentes consultarAusentes;
    
    public CheckInController(RealizarCheckIn realizarCheckIn,
                             ConsultarHospedesPresentes consultarPresentes,
                             ConsultarHospedesAusentes consultarAusentes) {
        this.realizarCheckIn = realizarCheckIn;
        this.consultarPresentes = consultarPresentes;
        this.consultarAusentes = consultarAusentes;
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CheckIn realizar(@RequestBody RealizarCheckInRequest req) {
        return realizarCheckIn.executar(
                req.idHospede(),
                req.dataEntrada(),
                req.dataSaida(),
                req.adicionalVeiculo()
        );
    }

    @GetMapping("/presentes")
    public List<GastoHospedeDTO> presentes() {
        return consultarPresentes.executar();
    }

    @GetMapping("/ausentes")
    public List<GastoHospedeDTO> ausentes() {
        return consultarAusentes.executar();
    }
    
}
