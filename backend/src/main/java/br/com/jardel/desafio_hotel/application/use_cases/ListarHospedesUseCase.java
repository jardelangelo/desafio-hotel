/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.jardel.desafio_hotel.application.use_cases;

import br.com.jardel.desafio_hotel.api.dtos.EmptyRequest;
import br.com.jardel.desafio_hotel.domain.models.Hospede;
import br.com.jardel.desafio_hotel.domain.repositories.IHospedeRepository;

import java.util.List;

/**
 *
 * @author jarde
 */

public class ListarHospedesUseCase implements IListarHospedesUseCase {

    private final IHospedeRepository hospedeRepositorio;

    public ListarHospedesUseCase(IHospedeRepository hospedeRepositorio) {
        this.hospedeRepositorio = hospedeRepositorio;
    }

    @Override
    public List<Hospede> execute(EmptyRequest request) {
        return hospedeRepositorio.listarTodos();
    }
}
