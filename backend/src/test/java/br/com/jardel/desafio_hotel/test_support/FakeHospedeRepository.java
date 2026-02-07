/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */

package br.com.jardel.desafio_hotel.test_support;

import br.com.jardel.desafio_hotel.domain.models.Hospede;
import br.com.jardel.desafio_hotel.domain.repositories.IHospedeRepository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class FakeHospedeRepository implements IHospedeRepository {

    private final Map<Long, Hospede> dados = new LinkedHashMap<>();
    private final AtomicLong seq = new AtomicLong(1);

    @Override
    public Hospede salvar(Hospede hospede) {
        if (hospede == null) throw new IllegalArgumentException("Hóspede é obrigatório.");

        Long id = hospede.id();
        if (id == null) {
            id = seq.getAndIncrement();
        }

        Hospede salvo = new Hospede(
                id,
                hospede.nome(),
                hospede.documento(),
                hospede.telefone()
        );

        dados.put(id, salvo);
        return salvo;
    }

    @Override
    public Optional<Hospede> buscarPorId(Long id) {
        if (id == null) return Optional.empty();
        return Optional.ofNullable(dados.get(id));
    }

    @Override
    public Optional<Hospede> buscarPorDocumento(String documento) {
        if (documento == null) return Optional.empty();
        String doc = documento.trim();

        for (Hospede h : dados.values()) {
            if (h.documento() != null && h.documento().trim().equals(doc)) {
                return Optional.of(h);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Hospede> listarTodos() {
        return new ArrayList<>(dados.values());
    }

    @Override
    public void excluirPorId(Long id) {
        if (id == null) return;
        dados.remove(id);
    }

    @Override
    public List<Hospede> buscarPorTermo(String termo) {
        if (termo == null) return List.of();
        String t = termo.trim().toLowerCase(Locale.ROOT);
        if (t.isEmpty()) return List.of();

        List<Hospede> resp = new ArrayList<>();
        for (Hospede h : dados.values()) {
            if (contém(h.nome(), t) || contém(h.documento(), t) || contém(h.telefone(), t)) {
                resp.add(h);
            }
        }
        return resp;
    }

    private boolean contém(String campo, String termoLower) {
        if (campo == null) return false;
        return campo.toLowerCase(Locale.ROOT).contains(termoLower);
    }
}
