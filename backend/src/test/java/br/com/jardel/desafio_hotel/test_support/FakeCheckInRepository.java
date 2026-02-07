/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */

package br.com.jardel.desafio_hotel.test_support;

import br.com.jardel.desafio_hotel.domain.models.CheckIn;
import br.com.jardel.desafio_hotel.domain.repositories.ICheckInRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class FakeCheckInRepository implements ICheckInRepository {

    private final Map<Long, CheckIn> dados = new LinkedHashMap<>();
    private final AtomicLong seq = new AtomicLong(1);

    // "agora" fixo para testes (p/ presentes/ausentes)
    private LocalDateTime agora;

    public FakeCheckInRepository(LocalDateTime agora) {
        this.agora = agora;
    }

    public void setAgora(LocalDateTime agora) {
        this.agora = agora;
    }

    @Override
    public CheckIn salvar(CheckIn checkIn) {
        if (checkIn == null) throw new IllegalArgumentException("Check-in é obrigatório.");

        Long id = checkIn.id();
        if (id == null) id = seq.getAndIncrement();

        CheckIn salvo = new CheckIn(
                id,
                checkIn.idHospede(),
                checkIn.dataEntrada(),
                checkIn.dataSaida(),
                checkIn.adicionalVeiculo(),
                checkIn.valorTotal()
        );

        dados.put(id, salvo);
        return salvo;
    }

    @Override
    public Optional<CheckIn> buscarPorId(Long id) {
        if (id == null) return Optional.empty();
        return Optional.ofNullable(dados.get(id));
    }

    @Override
    public boolean existeSobreposicao(Long idHospede, LocalDateTime novaEntrada, LocalDateTime novaSaida) {
        for (CheckIn c : dados.values()) {
            if (Objects.equals(c.idHospede(), idHospede) && sobrepoe(c, novaEntrada, novaSaida)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean existeSobreposicaoExcluindoId(Long idHospede, Long checkInId, LocalDateTime novaEntrada, LocalDateTime novaSaida) {
        for (CheckIn c : dados.values()) {
            if (Objects.equals(c.idHospede(), idHospede)
                    && !Objects.equals(c.id(), checkInId)
                    && sobrepoe(c, novaEntrada, novaSaida)) {
                return true;
            }
        }
        return false;
    }

    // mesma regra do JPA:
    // c.dataEntrada < novaSaida  AND  c.dataSaida > novaEntrada
    private boolean sobrepoe(CheckIn existente, LocalDateTime novaEntrada, LocalDateTime novaSaida) {
        return existente.dataEntrada().isBefore(novaSaida) && existente.dataSaida().isAfter(novaEntrada);
    }

    @Override
    public List<CheckIn> listarPresentes() {
        return listarPresentesPaginado(0, 200);
    }

    @Override
    public List<CheckIn> listarAusentes() {
        return listarAusentesPaginado(0, 200);
    }

    @Override
    public List<CheckIn> listarPorHospede(Long idHospede) {
        List<CheckIn> lista = new ArrayList<>();
        for (CheckIn c : dados.values()) {
            if (Objects.equals(c.idHospede(), idHospede)) {
                lista.add(c);
            }
        }
        lista.sort(Comparator.comparing(CheckIn::dataEntrada).reversed());
        return lista;
    }

    @Override
    public List<CheckIn> listarPresentesPaginado(int page, int size) {
        validarPaginacao(page, size);

        Map<Long, CheckIn> ultimoPorHospede = new HashMap<>();
        for (CheckIn c : dados.values()) {
            if (c.dataEntrada().isAfter(agora)) continue;              // entry_at <= agora
            if (!c.dataSaida().isAfter(agora)) continue;              // exit_at > agora
            CheckIn atual = ultimoPorHospede.get(c.idHospede());
            if (atual == null || c.dataSaida().isAfter(atual.dataSaida())) {
                ultimoPorHospede.put(c.idHospede(), c);
            }
        }

        List<CheckIn> escolhidos = new ArrayList<>(ultimoPorHospede.values());
        escolhidos.sort(Comparator.comparing(CheckIn::dataSaida).reversed());

        return paginar(escolhidos, page, size);
    }

    @Override
    public long contarPresentes() {
        Set<Long> distinct = new HashSet<>();
        for (CheckIn c : dados.values()) {
            if (!c.dataEntrada().isAfter(agora) && c.dataSaida().isAfter(agora)) {
                distinct.add(c.idHospede());
            }
        }
        return distinct.size();
    }

    @Override
    public List<CheckIn> listarAusentesPaginado(int page, int size) {
        validarPaginacao(page, size);

        Map<Long, CheckIn> ultimoPorHospede = new HashMap<>();
        for (CheckIn c : dados.values()) {
            if (c.dataSaida().isAfter(agora)) continue; // exit_at <= agora
            CheckIn atual = ultimoPorHospede.get(c.idHospede());
            if (atual == null || c.dataSaida().isAfter(atual.dataSaida())) {
                ultimoPorHospede.put(c.idHospede(), c);
            }
        }

        List<CheckIn> escolhidos = new ArrayList<>(ultimoPorHospede.values());
        escolhidos.sort(Comparator.comparing(CheckIn::dataSaida).reversed());

        return paginar(escolhidos, page, size);
    }

    @Override
    public long contarAusentes() {
        Set<Long> distinct = new HashSet<>();
        for (CheckIn c : dados.values()) {
            if (!c.dataSaida().isAfter(agora)) { // exit_at <= agora
                distinct.add(c.idHospede());
            }
        }
        return distinct.size();
    }

    @Override
    public BigDecimal somarTotalPorHospede(Long idHospede) {
        BigDecimal total = BigDecimal.ZERO;
        for (CheckIn c : dados.values()) {
            if (Objects.equals(c.idHospede(), idHospede)) {
                if (c.valorTotal() != null) total = total.add(c.valorTotal());
            }
        }
        return total;
    }

    private void validarPaginacao(int page, int size) {
        if (page < 0) throw new IllegalArgumentException("Página informada deve ser maior que zero.");
        if (size <= 0 || size > 200) throw new IllegalArgumentException("Quantidade de registros na página deve estar entre 1 e 200.");
    }

    private List<CheckIn> paginar(List<CheckIn> lista, int page, int size) {
        int offset = page * size;
        if (offset >= lista.size()) return List.of();
        int to = Math.min(lista.size(), offset + size);
        return lista.subList(offset, to);
    }
}
