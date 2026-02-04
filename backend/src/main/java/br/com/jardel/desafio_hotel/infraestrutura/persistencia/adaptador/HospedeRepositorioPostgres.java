/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.jardel.desafio_hotel.infraestrutura.persistencia.adaptador;

import br.com.jardel.desafio_hotel.dominio.modelo.Hospede;
import br.com.jardel.desafio_hotel.dominio.porta.HospedeRepositorio;
import br.com.jardel.desafio_hotel.infraestrutura.persistencia.entidade.HospedeEntidade;
import br.com.jardel.desafio_hotel.infraestrutura.persistencia.jpa.HospedeJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author jarde
 */

@Repository
public class HospedeRepositorioPostgres implements HospedeRepositorio {

    private final HospedeJpaRepository jpa;

    public HospedeRepositorioPostgres(HospedeJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Hospede salvar(Hospede hospede) {
        HospedeEntidade ent = new HospedeEntidade(
                hospede.id(),
                hospede.nome(),
                hospede.documento(),
                hospede.telefone()
        );
        HospedeEntidade salvo = jpa.save(ent);
        return new Hospede(salvo.getId(), salvo.getNome(), salvo.getDocumento(), salvo.getTelefone());
    }

    @Override
    public Optional<Hospede> buscarPorId(Long id) {
        return jpa.findById(id).map(this::toDominio);
    }

    @Override
    public Optional<Hospede> buscarPorDocumento(String documento) {
        return jpa.findByDocumento(documento).map(this::toDominio);
    }

    @Override
    public List<Hospede> listarTodos() {
        return jpa.findAll().stream().map(this::toDominio).toList();
    }

    @Override
    public void excluirPorId(Long id) {
        jpa.deleteById(id);
    }

    @Override
    public List<Hospede> buscarPorTermo(String termo) {
        return jpa.buscarPorTermo(termo).stream().map(this::toDominio).toList();
    }

    private Hospede toDominio(HospedeEntidade e) {
        return new Hospede(e.getId(), e.getNome(), e.getDocumento(), e.getTelefone());
    }
}
