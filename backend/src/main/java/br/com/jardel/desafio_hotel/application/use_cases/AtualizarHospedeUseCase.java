/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.jardel.desafio_hotel.application.use_cases;

import br.com.jardel.desafio_hotel.api.exceptions.NotFoundException;
import br.com.jardel.desafio_hotel.api.dtos.AtualizarHospedeRequest;
import br.com.jardel.desafio_hotel.domain.models.Hospede;
import br.com.jardel.desafio_hotel.domain.repositories.IHospedeRepository;

/**
 *
 * @author jarde
 */

public class AtualizarHospedeUseCase implements IAtualizarHospedeUseCase {

    private final IHospedeRepository hospedeRepositorio;

    public AtualizarHospedeUseCase(IHospedeRepository hospedeRepositorio) {
        this.hospedeRepositorio = hospedeRepositorio;
    }

    @Override
    public Hospede execute(AtualizarHospedeRequest request) {
        if (request == null || request.id() == null) throw new IllegalArgumentException("ID do hóspede é obrigatório.");

        Hospede atual = hospedeRepositorio.buscarPorId(request.id())
                .orElseThrow(() -> new NotFoundException("Hóspede não encontrado."));

        String nome = (request.nome() == null || request.nome().isBlank()) ? atual.nome() : request.nome().trim();
        String telefone = (request.telefone() == null || request.telefone().isBlank()) ? atual.telefone() : request.telefone().trim();

        Hospede atualizado = new Hospede(atual.id(), nome, atual.documento(), telefone);
        return hospedeRepositorio.salvar(atualizado);
    }
}
