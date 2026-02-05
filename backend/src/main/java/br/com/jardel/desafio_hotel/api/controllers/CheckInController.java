/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.jardel.desafio_hotel.api.controllers;

import br.com.jardel.desafio_hotel.api.dtos.RealizarCheckInRequest;
import br.com.jardel.desafio_hotel.application.use_cases.IConsultarHospedesAusentesUseCase;
import br.com.jardel.desafio_hotel.application.use_cases.IConsultarHospedesPresentesUseCase;
import br.com.jardel.desafio_hotel.application.use_cases.IRealizarCheckInUseCase;
import br.com.jardel.desafio_hotel.application.dtos.GastoHospedeDTO;
import br.com.jardel.desafio_hotel.domain.models.CheckIn;
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
    
    private final IRealizarCheckInUseCase realizarCheckIn;
    private final IConsultarHospedesPresentesUseCase consultarPresentes;
    private final IConsultarHospedesAusentesUseCase consultarAusentes;
    
    public CheckInController(IRealizarCheckInUseCase realizarCheckIn,
                             IConsultarHospedesPresentesUseCase consultarPresentes,
                             IConsultarHospedesAusentesUseCase consultarAusentes) {
        this.realizarCheckIn = realizarCheckIn;
        this.consultarPresentes = consultarPresentes;
        this.consultarAusentes = consultarAusentes;
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CheckIn realizar(@RequestBody RealizarCheckInRequest req) {
        return realizarCheckIn.execute(
                req.idHospede(),
                req.dataEntrada(),
                req.dataSaida(),
                req.adicionalVeiculo()
        );
    }

    @GetMapping("/presentes")
    public List<GastoHospedeDTO> presentes() {
        return consultarPresentes.execute();
    }

    @GetMapping("/ausentes")
    public List<GastoHospedeDTO> ausentes() {
        return consultarAusentes.execute();
    }
    
}
