/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */

package br.com.jardel.desafio_hotel.domain.services;

import br.com.jardel.desafio_hotel.api.dtos.RealizarCheckInRequest;
import br.com.jardel.desafio_hotel.api.exceptions.ConflictException;
import br.com.jardel.desafio_hotel.api.exceptions.NotFoundException;
import br.com.jardel.desafio_hotel.domain.models.CheckIn;
import br.com.jardel.desafio_hotel.domain.models.Hospede;
import br.com.jardel.desafio_hotel.test_support.FakeCheckInRepository;
import br.com.jardel.desafio_hotel.test_support.FakeHospedeRepository;
import br.com.jardel.desafio_hotel.application.use_cases.*;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author jarde
 */
public class RealizarCheckInUseCaseTest {

    private final FakeHospedeRepository hospedeRepositorio = new FakeHospedeRepository();
    private final FakeCheckInRepository checkInRepositorio = new FakeCheckInRepository(LocalDateTime.of(2026, 2, 7, 12, 0));
    private final ICalculadoraHospedagemService calculadora = new CalculadoraHospedagemService();

    private final IRealizarCheckInUseCase useCase = new RealizarCheckInUseCase(hospedeRepositorio, checkInRepositorio, calculadora);

    @Test
    void deveRealizarCheckInQuandoValido() {
        Hospede h = hospedeRepositorio.salvar(new Hospede(null, "Fulano", "123", "111"));

        LocalDateTime entrada = LocalDateTime.of(2026, 2, 2, 10, 0); // segunda
        LocalDateTime saida = LocalDateTime.of(2026, 2, 2, 12, 0);

        CheckIn salvo = useCase.execute(new RealizarCheckInRequest(h.id(), entrada, saida, false));

        assertNotNull(salvo.id());
        assertEquals(h.id(), salvo.idHospede());
        assertEquals(new BigDecimal("120.00"), salvo.valorTotal());
    }

    @Test
    void deveFalharQuandoHospedeNaoExiste() {
        NotFoundException ex = assertThrows(NotFoundException.class, () ->
                useCase.execute(new RealizarCheckInRequest(
                        999L,
                        LocalDateTime.of(2026, 2, 2, 10, 0),
                        LocalDateTime.of(2026, 2, 2, 12, 0),
                        false
                ))
        );

        assertEquals("Hóspede não encontrado.", ex.getMessage());
    }

    @Test
    void deveFalharQuandoHaSobreposicao() {
        Hospede h = hospedeRepositorio.salvar(new Hospede(null, "Fulano", "123", "111"));

        // check-in existente
        checkInRepositorio.salvar(new CheckIn(
                null, h.id(),
                LocalDateTime.of(2026, 2, 2, 10, 0),
                LocalDateTime.of(2026, 2, 3, 10, 0),
                false,
                new BigDecimal("120.00")
        ));

        ConflictException ex = assertThrows(ConflictException.class, () ->
                useCase.execute(new RealizarCheckInRequest(
                        h.id(),
                        LocalDateTime.of(2026, 2, 2, 12, 0),
                        LocalDateTime.of(2026, 2, 2, 13, 0),
                        false
                ))
        );

        assertEquals("O hóspede já possui uma hospedagem no período informado.", ex.getMessage());
    }

    @Test
    void deveFalharQuandoDataSaidaAntesDaEntrada() {
        Hospede h = hospedeRepositorio.salvar(new Hospede(null, "Fulano", "123", "111"));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                useCase.execute(new RealizarCheckInRequest(
                        h.id(),
                        LocalDateTime.of(2026, 2, 3, 10, 0),
                        LocalDateTime.of(2026, 2, 2, 10, 0),
                        false
                ))
        );

        assertEquals("A data de saída do check-in deve ser maior ou igual à data de entrada.", ex.getMessage());
    }

    @Test
    void deveFalharQuandoCamposObrigatoriosAusentes() {
        assertEquals("Requisição inválida: corpo da requisição (request) é obrigatório.",
                assertThrows(IllegalArgumentException.class, () -> useCase.execute(null)).getMessage());

        assertEquals("Hóspede é obrigatório.",
                assertThrows(IllegalArgumentException.class,
                        () -> useCase.execute(new RealizarCheckInRequest(
                                null,
                                LocalDateTime.of(2026, 2, 2, 10, 0),
                                LocalDateTime.of(2026, 2, 2, 12, 0),
                                false
                        ))).getMessage());
    }

    @Test
    void deveFalharQuandoDataEntradaNoFuturo_seRegraEstiverAtiva() {
        // Esse teste só passa se você adicionou a regra "dataEntrada não pode ser no futuro" no UseCase.
        Hospede h = hospedeRepositorio.salvar(new Hospede(null, "Fulano", "123", "111"));

        LocalDateTime futura = LocalDateTime.now().plusDays(1);
        LocalDateTime saida = futura.plusHours(2);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                useCase.execute(new RealizarCheckInRequest(h.id(), futura, saida, false))
        );

        assertTrue(ex.getMessage().toLowerCase().contains("futuro"));
    }
}
