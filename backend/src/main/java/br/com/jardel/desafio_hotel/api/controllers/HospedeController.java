/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.jardel.desafio_hotel.api.controllers;

import br.com.jardel.desafio_hotel.api.dtos.CadastrarHospedeRequest;
import br.com.jardel.desafio_hotel.api.dtos.HospedeResponse;
import br.com.jardel.desafio_hotel.application.use_cases.ICadastrarHospedeUseCase;
import br.com.jardel.desafio_hotel.domain.models.Hospede;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import br.com.jardel.desafio_hotel.api.dtos.AtualizarHospedeRequest;
import br.com.jardel.desafio_hotel.application.use_cases.*;
import br.com.jardel.desafio_hotel.api.dtos.*;

import java.util.List;

/**
 *
 * @author jarde
 */

@RestController
@RequestMapping("/api/hospedes")
public class HospedeController {

    private final ICadastrarHospedeUseCase cadastrarHospede;
    private final IBuscarHospedePorIdUseCase buscarHospedePorId;
    private final IListarHospedesUseCase listarHospedes;
    private final IBuscarHospedesPorTermoUseCase buscarHospedesPorTermo;
    private final IAtualizarHospedeUseCase atualizarHospede;
    private final IExcluirHospedeUseCase excluirHospede;

    public HospedeController(ICadastrarHospedeUseCase cadastrarHospede,
                             IBuscarHospedePorIdUseCase buscarHospedePorId,
                             IListarHospedesUseCase listarHospedes,
                             IBuscarHospedesPorTermoUseCase buscarHospedesPorTermo,
                             IAtualizarHospedeUseCase atualizarHospede,
                             IExcluirHospedeUseCase excluirHospede) {
        this.cadastrarHospede = cadastrarHospede;
        this.buscarHospedePorId = buscarHospedePorId;
        this.listarHospedes = listarHospedes;
        this.buscarHospedesPorTermo = buscarHospedesPorTermo;
        this.atualizarHospede = atualizarHospede;
        this.excluirHospede = excluirHospede;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public HospedeResponse cadastrar(@RequestBody CadastrarHospedeRequest req) {
        Hospede salvo = cadastrarHospede.execute(
                new br.com.jardel.desafio_hotel.api.dtos.CadastrarHospedeRequest(
                        req.nome(), req.documento(), req.telefone()
                )
        );
        return toResponse(salvo);
    }

    @GetMapping("/{id}")
    public HospedeResponse buscarPorId(@PathVariable Long id) {
        Hospede hospede = buscarHospedePorId.execute(
                new br.com.jardel.desafio_hotel.api.dtos.BuscarHospedePorIdRequest(id)
        );
        return toResponse(hospede);
    }

    @GetMapping
    public List<HospedeResponse> listar() {
        return listarHospedes.execute(new EmptyRequest()).stream()
                .map(this::toResponse)
                .toList();
    }

    @GetMapping("/buscar")
    public List<HospedeResponse> buscar(@RequestParam String termo) {
        return buscarHospedesPorTermo.execute(
                new br.com.jardel.desafio_hotel.api.dtos.BuscarHospedesPorTermoRequest(termo)
        ).stream().map(this::toResponse).toList();
    }

    @PutMapping("/{id}")
    public HospedeResponse atualizar(@PathVariable Long id, @RequestBody AtualizarHospedeRequest req) {
        Hospede salvo = atualizarHospede.execute(
                new br.com.jardel.desafio_hotel.api.dtos.AtualizarHospedeRequest(
                        id, req.nome(), req.telefone()
                )
        );
        return toResponse(salvo);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        excluirHospede.execute(
                new br.com.jardel.desafio_hotel.api.dtos.ExcluirHospedeRequest(id)
        );
    }

    private HospedeResponse toResponse(Hospede h) {
        return new HospedeResponse(h.id(), h.nome(), h.documento(), h.telefone());
    }
    
}
