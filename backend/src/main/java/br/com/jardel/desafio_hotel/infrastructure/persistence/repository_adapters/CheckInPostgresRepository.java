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
import java.util.*;
import java.math.BigDecimal;
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
                checkIn.adicionalVeiculo(),
                checkIn.valorTotal()
        );
        CheckInEntity salvo = jpa.save(ent);
        return toDominio(salvo);
    }

    @Override
    public Optional<CheckIn> buscarPorId(Long id) {
        return jpa.findById(id).map(this::toDominio);
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

    @Override
    public boolean existeSobreposicao(Long idHospede, LocalDateTime novaEntrada, LocalDateTime novaSaida) {
        return jpa.existeSobreposicao(idHospede, novaEntrada, novaSaida);
    }

    @Override
    public boolean existeSobreposicaoExcluindoId(Long idHospede, Long checkInId, LocalDateTime novaEntrada, LocalDateTime novaSaida) {
        return jpa.existeSobreposicaoExcluindoId(idHospede, checkInId, novaEntrada, novaSaida);
    }
    
    @Override
    public List<CheckIn> listarPresentesPaginado(int page, int size) {
        validarPaginacao(page, size);
        int offset = page * size;
        var agora = LocalDateTime.now();
        return jpa.listarPresentesPaginado(agora, size, offset).stream().map(this::toDominio).toList();
    }

    @Override
    public long contarPresentes() {
        return jpa.contarPresentes(LocalDateTime.now());
    }

    @Override
    public List<CheckIn> listarAusentesPaginado(int page, int size) {
        validarPaginacao(page, size);
        int offset = page * size;
        var agora = LocalDateTime.now();
        return jpa.listarAusentesPaginado(agora, size, offset).stream().map(this::toDominio).toList();
    }

    @Override
    public long contarAusentes() {
        return jpa.contarAusentes(LocalDateTime.now());
    }

    @Override
    public BigDecimal somarTotalPorHospede(Long idHospede) {
        return jpa.somarTotalPorHospede(idHospede);
    }
    
    private CheckIn toDominio(CheckInEntity e) {
        return new CheckIn(
                e.getId(), 
                e.getIdHospede(), 
                e.getDataEntrada(), 
                e.getDataSaida(), 
                e.isAdicionalVeiculo(), 
                e.getValorTotal());
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
    
    private void validarPaginacao(int page, int size) {
        if (page < 0) throw new IllegalArgumentException("Página informada deve ser maior que zero.");
        if (size <= 0 || size > 200) throw new IllegalArgumentException("Quantidade de registros na página deve estar entre 1 e 200.");
    }
}
