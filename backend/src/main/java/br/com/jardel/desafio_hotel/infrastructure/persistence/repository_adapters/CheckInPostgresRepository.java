/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.jardel.desafio_hotel.infrastructure.persistence.repository_adapters;

import br.com.jardel.desafio_hotel.domain.models.CheckIn;
import br.com.jardel.desafio_hotel.domain.repositories.ICheckInRepository;
import br.com.jardel.desafio_hotel.infrastructure.persistence.entities.CheckInEntity;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import br.com.jardel.desafio_hotel.infrastructure.persistence.jpa_repositories.ICheckInJpaRepository;

/**
 *
 * @author jarde
 */

@Repository
public class CheckInPostgresRepository implements ICheckInRepository {

    private final ICheckInJpaRepository jpa;

    public CheckInPostgresRepository(ICheckInJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public CheckIn salvar(CheckIn checkIn) {
        CheckInEntity ent = new CheckInEntity(
                checkIn.id(),
                checkIn.idHospede(),
                checkIn.dataEntrada(),
                checkIn.dataSaida(),
                checkIn.adicionalVeiculo()
        );
        CheckInEntity salvo = jpa.save(ent);
        return toDominio(salvo);
    }

    @Override
    public List<CheckIn> listarPresentes() {
        LocalDateTime agora = LocalDateTime.now();
        var listaOrdenada = jpa.listarPresentesOrdenado(agora);
        return manterApenasUltimoPorHospede(listaOrdenada);
    }

    @Override
    public List<CheckIn> listarAusentes() {
        LocalDateTime agora = LocalDateTime.now();
        var listaOrdenada = jpa.listarAusentesOrdenado(agora);
        return manterApenasUltimoPorHospede(listaOrdenada);
    }

    @Override
    public List<CheckIn> listarPorHospede(Long idHospede) {
        return jpa.findByIdHospedeOrderByDataEntradaDesc(idHospede).stream().map(this::toDominio).toList();
    }

    private CheckIn toDominio(CheckInEntity e) {
        return new CheckIn(e.getId(), e.getIdHospede(), e.getDataEntrada(), e.getDataSaida(), e.isAdicionalVeiculo());
    }
    
    private List<CheckIn> manterApenasUltimoPorHospede(List<CheckInEntity> listaOrdenada) {
        Set<Long> vistos = new HashSet<>();
        List<CheckIn> resultado = new ArrayList<>();

        for (CheckInEntity e : listaOrdenada) {
            if (vistos.add(e.getIdHospede())) {
                resultado.add(toDominio(e));
            }
        }
        return resultado;
    }
}
