/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */

package br.com.jardel.desafio_hotel.domain.services;

import br.com.jardel.desafio_hotel.api.dtos.ExcluirHospedeRequest;
import br.com.jardel.desafio_hotel.api.exceptions.ConflictException;
import br.com.jardel.desafio_hotel.api.exceptions.NotFoundException;
import br.com.jardel.desafio_hotel.domain.models.CheckIn;
import br.com.jardel.desafio_hotel.domain.models.Hospede;
import br.com.jardel.desafio_hotel.test_support.FakeCheckInRepository;
import br.com.jardel.desafio_hotel.test_support.FakeHospedeRepository;
import br.com.jardel.desafio_hotel.application.use_cases.*;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author jarde
 */
public class ExcluirHospedeUseCaseTest {

    private final FakeHospedeRepository hospedeRepositorio = new FakeHospedeRepository();
    private final FakeCheckInRepository checkInRepositorio = new FakeCheckInRepository(LocalDateTime.of(2026, 2, 7, 12, 0));

    private final IExcluirHospedeUseCase useCase = new ExcluirHospedeUseCase(hospedeRepositorio, checkInRepositorio);

    @Test
    void deveExcluirQuandoExisteESemCheckins() {
        Hospede h = hospedeRepositorio.salvar(new Hospede(null, "Fulano", "123", "111"));

        useCase.execute(new ExcluirHospedeRequest(h.id()));

        assertTrue(hospedeRepositorio.buscarPorId(h.id()).isEmpty());
    }

    @Test
    void deveFalharQuandoHospedeNaoExiste() {
        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> useCase.execute(new ExcluirHospedeRequest(999L)));
        assertEquals("Hóspede não encontrado.", ex.getMessage());
    }

    @Test
    void deveFalharQuandoHospedeTemCheckins() {
        Hospede h = hospedeRepositorio.salvar(new Hospede(null, "Fulano", "123", "111"));

        checkInRepositorio.salvar(new CheckIn(
                null, h.id(),
                LocalDateTime.of(2026, 2, 1, 10, 0),
                LocalDateTime.of(2026, 2, 2, 10, 0),
                false,
                null
        ));

        ConflictException ex = assertThrows(ConflictException.class,
                () -> useCase.execute(new ExcluirHospedeRequest(h.id())));
        assertEquals("Não é possível excluir o hóspede, pois existem check-ins vinculados a ele.", ex.getMessage());
    }

    @Test
    void deveFalharQuandoIdObrigatorio() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> useCase.execute(new ExcluirHospedeRequest(null)));
        assertEquals("ID do hóspede é obrigatório.", ex.getMessage());
    }
}
