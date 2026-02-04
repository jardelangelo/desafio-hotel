/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.jardel.desafio_hotel.api.controller;

import br.com.jardel.desafio_hotel.api.dto.CadastrarHospedeRequest;
import br.com.jardel.desafio_hotel.api.dto.HospedeResponse;
import br.com.jardel.desafio_hotel.aplicacao.caso_uso.CadastrarHospede;
import br.com.jardel.desafio_hotel.dominio.modelo.Hospede;
import br.com.jardel.desafio_hotel.dominio.porta.HospedeRepositorio;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import br.com.jardel.desafio_hotel.api.dto.AtualizarHospedeRequest;
import br.com.jardel.desafio_hotel.api.erro.NaoEncontradoException;

import java.util.List;

/**
 *
 * @author jarde
 */

@RestController
@RequestMapping("/api/hospedes")
public class HospedeController {
    
    private final CadastrarHospede cadastrarHospede;
    private final HospedeRepositorio hospedeRepositorio;
    
    public HospedeController(CadastrarHospede cadastrarHospede,
                             HospedeRepositorio hospedeRepositorio) {
        this.cadastrarHospede = cadastrarHospede;
        this.hospedeRepositorio = hospedeRepositorio;
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public HospedeResponse cadastrar(@RequestBody CadastrarHospedeRequest req) {
        Hospede salvo = cadastrarHospede.executar(req.nome(), req.documento(), req.telefone());
        return toResponse(salvo);
    }

    @GetMapping("/{id}")
    public HospedeResponse buscarPorId(@PathVariable Long id) {
        Hospede hospede = hospedeRepositorio.buscarPorId(id)
                .orElseThrow(() -> new NaoEncontradoException("hospede nao encontrado"));
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
                .orElseThrow(() -> new NaoEncontradoException("hospede nao encontrado"));

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
