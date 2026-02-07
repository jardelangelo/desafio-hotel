/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */

package br.com.jardel.desafio_hotel.domain.services;

import br.com.jardel.desafio_hotel.api.dtos.AtualizarCheckInRequest;
import br.com.jardel.desafio_hotel.api.exceptions.ConflictException;
import br.com.jardel.desafio_hotel.api.exceptions.NotFoundException;
import br.com.jardel.desafio_hotel.domain.models.CheckIn;
import br.com.jardel.desafio_hotel.test_support.FakeCheckInRepository;
import br.com.jardel.desafio_hotel.application.use_cases.*;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author jarde
 */
public class AtualizarCheckInUseCaseTest {

    private final FakeCheckInRepository checkInRepositorio = new FakeCheckInRepository(LocalDateTime.of(2026, 2, 7, 12, 0));
    private final ICalculadoraHospedagemService calculadora = new CalculadoraHospedagemService();

    private final IAtualizarCheckInUseCase useCase = new AtualizarCheckInUseCase(checkInRepositorio, calculadora);

    @Test
    void deveAtualizarCamposERecalcularTotal() {
        CheckIn atual = checkInRepositorio.salvar(new CheckIn(
                null, 1L,
                LocalDateTime.of(2026, 2, 2, 10, 0), // segunda
                LocalDateTime.of(2026, 2, 2, 12, 0),
                false,
                new BigDecimal("120.00")
        ));

        LocalDateTime novaSaida = LocalDateTime.of(2026, 2, 2, 17, 0); // após 16:30
        CheckIn atualizado = useCase.execute(new AtualizarCheckInRequest(atual.id(), null, novaSaida, true));

        assertEquals(atual.id(), atualizado.id());
        assertEquals(1L, atualizado.idHospede());
        assertTrue(atualizado.adicionalVeiculo());
        assertEquals(novaSaida, atualizado.dataSaida());

        // segunda-feira com veículo: (120 + 15) * 2 = 270
        assertEquals(new BigDecimal("270.00"), atualizado.valorTotal());
    }

    @Test
    void deveFalharQuandoNaoEncontrado() {
        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> useCase.execute(new AtualizarCheckInRequest(999L, null, null, null)));

        assertEquals("Check-in não encontrado.", ex.getMessage());
    }

    @Test
    void deveFalharQuandoDatasInvalidas() {
        CheckIn atual = checkInRepositorio.salvar(new CheckIn(
                null, 1L,
                LocalDateTime.of(2026, 2, 2, 10, 0),
                LocalDateTime.of(2026, 2, 2, 12, 0),
                false,
                null
        ));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> useCase.execute(new AtualizarCheckInRequest(
                        atual.id(),
                        LocalDateTime.of(2026, 2, 3, 10, 0),
                        LocalDateTime.of(2026, 2, 2, 10, 0),
                        null
                )));

        assertEquals("A data de saída do check-in deve ser maior ou igual à data de entrada.", ex.getMessage());
    }

    @Test
    void deveFalharQuandoHaSobreposicaoComOutroCheckin() {
        CheckIn c1 = checkInRepositorio.salvar(new CheckIn(
                null, 1L,
                LocalDateTime.of(2026, 2, 2, 10, 0),
                LocalDateTime.of(2026, 2, 2, 12, 0),
                false,
                null
        ));

        // outro checkin do mesmo hóspede que sobrepõe o período
        checkInRepositorio.salvar(new CheckIn(
                null, 1L,
                LocalDateTime.of(2026, 2, 2, 11, 0),
                LocalDateTime.of(2026, 2, 2, 13, 0),
                false,
                null
        ));

        ConflictException ex = assertThrows(ConflictException.class,
                () -> useCase.execute(new AtualizarCheckInRequest(
                        c1.id(),
                        LocalDateTime.of(2026, 2, 2, 10, 30),
                        LocalDateTime.of(2026, 2, 2, 12, 30),
                        null
                )));

        assertEquals("O hóspede já possui uma hospedagem no período informado.", ex.getMessage());
    }

    @Test
    void deveFalharQuandoIdObrigatorio() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> useCase.execute(new AtualizarCheckInRequest(null, null, null, null)));
        assertEquals("ID do check-in é obrigatório.", ex.getMessage());
    }
}
