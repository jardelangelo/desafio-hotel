/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.jardel.desafio_hotel.infraestrutura.persistencia.adaptador;

import br.com.jardel.desafio_hotel.dominio.modelo.CheckIn;
import br.com.jardel.desafio_hotel.dominio.porta.CheckInRepositorio;
import br.com.jardel.desafio_hotel.infraestrutura.persistencia.entidade.CheckInEntidade;
import br.com.jardel.desafio_hotel.infraestrutura.persistencia.jpa.CheckInJpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author jarde
 */

@Repository
public class CheckInRepositorioPostgres implements CheckInRepositorio {

    private final CheckInJpaRepository jpa;

    public CheckInRepositorioPostgres(CheckInJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public CheckIn salvar(CheckIn checkIn) {
        CheckInEntidade ent = new CheckInEntidade(
                checkIn.id(),
                checkIn.idHospede(),
                checkIn.dataEntrada(),
                checkIn.dataSaida(),
                checkIn.adicionalVeiculo()
        );
        CheckInEntidade salvo = jpa.save(ent);
        return toDominio(salvo);
    }

    @Override
    public List<CheckIn> listarPresentes() {
        LocalDateTime agora = LocalDateTime.now();
        var listaOrdenada = jpa.listarPresentes(agora);
        return manterApenasUltimoPorHospede(listaOrdenada);
    }

    @Override
    public List<CheckIn> listarAusentes() {
        LocalDateTime agora = LocalDateTime.now();
        var listaOrdenada = jpa.listarAusentes(agora);
        return manterApenasUltimoPorHospede(listaOrdenada);
    }

    @Override
    public List<CheckIn> listarPorHospede(Long idHospede) {
        return jpa.findByIdHospedeOrderByDataEntradaDesc(idHospede).stream().map(this::toDominio).toList();
    }

    private CheckIn toDominio(CheckInEntidade e) {
        return new CheckIn(e.getId(), e.getIdHospede(), e.getDataEntrada(), e.getDataSaida(), e.isAdicionalVeiculo());
    }
    
    private List<CheckIn> manterApenasUltimoPorHospede(List<CheckInEntidade> listaOrdenada) {
        Set<Long> vistos = new HashSet<>();
        List<CheckIn> resultado = new ArrayList<>();

        for (CheckInEntidade e : listaOrdenada) {
            if (vistos.add(e.getIdHospede())) {
                resultado.add(toDominio(e));
            }
        }
        return resultado;
    }
}
