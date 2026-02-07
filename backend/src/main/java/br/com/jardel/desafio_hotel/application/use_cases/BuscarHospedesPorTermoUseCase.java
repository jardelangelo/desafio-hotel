/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.jardel.desafio_hotel.application.use_cases;

import br.com.jardel.desafio_hotel.api.dtos.BuscarHospedesPorTermoRequest;
import br.com.jardel.desafio_hotel.domain.models.Hospede;
import br.com.jardel.desafio_hotel.domain.repositories.IHospedeRepository;

import java.util.List;

/**
 *
 * @author jarde
 */

public class BuscarHospedesPorTermoUseCase implements IBuscarHospedesPorTermoUseCase {

    private final IHospedeRepository hospedeRepositorio;

    public BuscarHospedesPorTermoUseCase(IHospedeRepository hospedeRepositorio) {
        this.hospedeRepositorio = hospedeRepositorio;
    }

    @Override
    public List<Hospede> execute(BuscarHospedesPorTermoRequest request) {
        if (request == null) throw new IllegalArgumentException("Requisição inválida: corpo da requisição (request) é obrigatório.");

        String termo = request.termo();
        if (termo == null || termo.isBlank()) throw new IllegalArgumentException("Termo para busca é obrigatório.");

        return hospedeRepositorio.buscarPorTermo(termo);
    }
}
