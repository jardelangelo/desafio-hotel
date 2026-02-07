/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.jardel.desafio_hotel.application.use_cases;

import br.com.jardel.desafio_hotel.api.exceptions.ConflictException;
import br.com.jardel.desafio_hotel.api.exceptions.NotFoundException;
import br.com.jardel.desafio_hotel.api.dtos.ExcluirHospedeRequest;
import br.com.jardel.desafio_hotel.domain.repositories.ICheckInRepository;
import br.com.jardel.desafio_hotel.domain.repositories.IHospedeRepository;

/**
 *
 * @author jarde
 */

public class ExcluirHospedeUseCase implements IExcluirHospedeUseCase {

    private final IHospedeRepository hospedeRepositorio;
    private final ICheckInRepository checkInRepositorio;

    public ExcluirHospedeUseCase(IHospedeRepository hospedeRepositorio, ICheckInRepository checkInRepositorio) {
        this.hospedeRepositorio = hospedeRepositorio;
        this.checkInRepositorio = checkInRepositorio;
    }

    @Override
    public Void execute(ExcluirHospedeRequest request) {
        if (request == null || request.id() == null) throw new IllegalArgumentException("ID do hóspede é obrigatório.");

        Long id = request.id();

        hospedeRepositorio.buscarPorId(id)
                .orElseThrow(() -> new NotFoundException("Hóspede não encontrado."));

        if (!checkInRepositorio.listarPorHospede(id).isEmpty()) {
            throw new ConflictException("Não é possível excluir o hóspede, pois existem check-ins vinculados a ele.");
        }

        hospedeRepositorio.excluirPorId(id);
        return null;
    }
}
