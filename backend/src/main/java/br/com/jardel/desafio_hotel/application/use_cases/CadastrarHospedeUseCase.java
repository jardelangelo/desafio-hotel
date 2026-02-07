/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.jardel.desafio_hotel.application.use_cases;

import br.com.jardel.desafio_hotel.api.dtos.CadastrarHospedeRequest;
import br.com.jardel.desafio_hotel.domain.models.Hospede;
import br.com.jardel.desafio_hotel.domain.repositories.IHospedeRepository;

/**
 *
 * @author jarde
 */

public class CadastrarHospedeUseCase implements ICadastrarHospedeUseCase  {
   
    private final IHospedeRepository hospedeRepositorio;

    public CadastrarHospedeUseCase(IHospedeRepository hospedeRepositorio) {
        this.hospedeRepositorio = hospedeRepositorio;
    }

    @Override
    public Hospede execute(CadastrarHospedeRequest request) {
        if (request == null) throw new IllegalArgumentException("Requisição inválida: corpo da requisição (request) é obrigatório.");

        String nome = request.nome();
        String documento = request.documento();
        String telefone = request.telefone();

        if (nome == null || nome.isBlank()) throw new IllegalArgumentException("Nome do hóspede é obrigatório.");
        if (documento == null || documento.isBlank()) throw new IllegalArgumentException("Documento do hóspede é obrigatório.");
        if (telefone == null || telefone.isBlank()) throw new IllegalArgumentException("Telefone do hóspede é obrigatório.");

        hospedeRepositorio.buscarPorDocumento(documento).ifPresent(h -> {
            throw new IllegalArgumentException("Já existe um hóspede cadastrado com este documento.");
        });

        Hospede novo = new Hospede(null, nome.trim(), documento.trim(), telefone.trim());
        return hospedeRepositorio.salvar(novo);
    }
    
}
