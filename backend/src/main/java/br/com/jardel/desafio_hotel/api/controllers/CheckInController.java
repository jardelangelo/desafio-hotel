/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.jardel.desafio_hotel.api.controllers;

import br.com.jardel.desafio_hotel.api.dtos.RealizarCheckInRequest;
import br.com.jardel.desafio_hotel.api.dtos.AtualizarCheckInRequest;
import br.com.jardel.desafio_hotel.application.dtos.GastoHospedeDTO;
import br.com.jardel.desafio_hotel.application.use_cases.IAtualizarCheckInUseCase;
import br.com.jardel.desafio_hotel.application.use_cases.IConsultarHospedesAusentesUseCase;
import br.com.jardel.desafio_hotel.application.use_cases.IConsultarHospedesPresentesUseCase;
import br.com.jardel.desafio_hotel.application.use_cases.IRealizarCheckInUseCase;
import br.com.jardel.desafio_hotel.api.dtos.PagedResult;
import br.com.jardel.desafio_hotel.api.dtos.PaginacaoRequest;
import br.com.jardel.desafio_hotel.domain.models.CheckIn;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author jarde
 */

@RestController
@RequestMapping("/api/checkins")
public class CheckInController {

    private final IRealizarCheckInUseCase realizarCheckIn;
    private final IAtualizarCheckInUseCase atualizarCheckIn;
    private final IConsultarHospedesPresentesUseCase consultarPresentes;
    private final IConsultarHospedesAusentesUseCase consultarAusentes;

    public CheckInController(IRealizarCheckInUseCase realizarCheckIn,
                             IAtualizarCheckInUseCase atualizarCheckIn,
                             IConsultarHospedesPresentesUseCase consultarPresentes,
                             IConsultarHospedesAusentesUseCase consultarAusentes) {
        this.realizarCheckIn = realizarCheckIn;
        this.atualizarCheckIn = atualizarCheckIn;
        this.consultarPresentes = consultarPresentes;
        this.consultarAusentes = consultarAusentes;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CheckIn realizar(@RequestBody RealizarCheckInRequest req) {
        return realizarCheckIn.execute(
                new br.com.jardel.desafio_hotel.api.dtos.RealizarCheckInRequest(
                        req.idHospede(),
                        req.dataEntrada(),
                        req.dataSaida(),
                        req.adicionalVeiculo()
                )
        );
    }

    @PutMapping("/{id}")
    public CheckIn atualizar(@PathVariable Long id, @RequestBody AtualizarCheckInRequest req) {
        return atualizarCheckIn.execute(
                new br.com.jardel.desafio_hotel.api.dtos.AtualizarCheckInRequest(
                        id,
                        req.dataEntrada(),
                        req.dataSaida(),
                        req.adicionalVeiculo()
                )
        );
    }

    @GetMapping("/presentes")
    public PagedResult<GastoHospedeDTO> presentes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return consultarPresentes.execute(new PaginacaoRequest(page, size));
    }

    @GetMapping("/ausentes")
    public PagedResult<GastoHospedeDTO> ausentes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return consultarAusentes.execute(new PaginacaoRequest(page, size));
    }
    
}
