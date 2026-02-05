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
import br.com.jardel.desafio_hotel.api.exceptions.NotFoundException;

import java.util.List;
import br.com.jardel.desafio_hotel.domain.repositories.IHospedeRepository;

/**
 *
 * @author jarde
 */

@RestController
@RequestMapping("/api/hospedes")
public class HospedeController {
    
    private final ICadastrarHospedeUseCase cadastrarHospede;
    private final IHospedeRepository hospedeRepositorio;
    
    public HospedeController(ICadastrarHospedeUseCase cadastrarHospede,
                             IHospedeRepository hospedeRepositorio) {
        this.cadastrarHospede = cadastrarHospede;
        this.hospedeRepositorio = hospedeRepositorio;
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public HospedeResponse cadastrar(@RequestBody CadastrarHospedeRequest req) {
        Hospede salvo = cadastrarHospede.execute(req.nome(), req.documento(), req.telefone());
        return toResponse(salvo);
    }

    @GetMapping("/{id}")
    public HospedeResponse buscarPorId(@PathVariable Long id) {
        Hospede hospede = hospedeRepositorio.buscarPorId(id)
                .orElseThrow(() -> new NotFoundException("hospede nao encontrado"));
        return toResponse(hospede);
    }

    @GetMapping
    public List<HospedeResponse> listar() {
        return hospedeRepositorio.listarTodos().stream().map(this::toResponse).toList();
    }
    
    @GetMapping("/buscar")
    public List<HospedeResponse> buscar(@RequestParam String termo) {
        return hospedeRepositorio.buscarPorTermo(termo).stream().map(this::toResponse).toList();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        hospedeRepositorio.excluirPorId(id);
    }

    @PutMapping("/{id}")
    public HospedeResponse atualizar(@PathVariable Long id, @RequestBody AtualizarHospedeRequest req) {
        Hospede atual = hospedeRepositorio.buscarPorId(id)
                .orElseThrow(() -> new NotFoundException("hospede nao encontrado"));

        String nome = (req.nome() == null || req.nome().isBlank()) ? atual.nome() : req.nome().trim();
        String telefone = (req.telefone() == null || req.telefone().isBlank()) ? atual.telefone() : req.telefone().trim();

        Hospede atualizado = new Hospede(atual.id(), nome, atual.documento(), telefone);
        Hospede salvo = hospedeRepositorio.salvar(atualizado);

        return toResponse(salvo);
    }
    
    private HospedeResponse toResponse(Hospede h) {
        return new HospedeResponse(h.id(), h.nome(), h.documento(), h.telefone());
    }
    
}
