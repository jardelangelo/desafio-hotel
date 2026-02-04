/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.jardel.desafio_hotel.aplicacao.caso_uso;

import br.com.jardel.desafio_hotel.dominio.modelo.Hospede;
import br.com.jardel.desafio_hotel.dominio.porta.HospedeRepositorio;

/**
 *
 * @author jarde
 */
public class CadastrarHospede {
   
    private final HospedeRepositorio hospedeRepositorio;

    public CadastrarHospede(HospedeRepositorio hospedeRepositorio) {
        this.hospedeRepositorio = hospedeRepositorio;
    }
    
    public Hospede executar(String nome, String documento, String telefone) {
        if (nome == null || nome.isBlank()) throw new IllegalArgumentException("nome obrigatorio");
        if (documento == null || documento.isBlank()) throw new IllegalArgumentException("documento obrigatorio");
        if (telefone == null || telefone.isBlank()) throw new IllegalArgumentException("telefone obrigatorio");

        // opcional: impedir duplicado por documento
        hospedeRepositorio.buscarPorDocumento(documento).ifPresent(h ->
                { throw new IllegalArgumentException("documento ja cadastrado"); }
        );

        Hospede novo = new Hospede(null, nome.trim(), documento.trim(), telefone.trim());
        return hospedeRepositorio.salvar(novo);
    }
    
}
