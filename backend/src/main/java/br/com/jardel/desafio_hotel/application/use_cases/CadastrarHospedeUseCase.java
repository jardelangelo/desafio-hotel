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
        if (request == null) throw new IllegalArgumentException("request obrigatorio");

        String nome = request.nome();
        String documento = request.documento();
        String telefone = request.telefone();

        if (nome == null || nome.isBlank()) throw new IllegalArgumentException("nome obrigatorio");
        if (documento == null || documento.isBlank()) throw new IllegalArgumentException("documento obrigatorio");
        if (telefone == null || telefone.isBlank()) throw new IllegalArgumentException("telefone obrigatorio");

        hospedeRepositorio.buscarPorDocumento(documento).ifPresent(h -> {
            throw new IllegalArgumentException("documento ja cadastrado");
        });

        Hospede novo = new Hospede(null, nome.trim(), documento.trim(), telefone.trim());
        return hospedeRepositorio.salvar(novo);
    }
    
}
