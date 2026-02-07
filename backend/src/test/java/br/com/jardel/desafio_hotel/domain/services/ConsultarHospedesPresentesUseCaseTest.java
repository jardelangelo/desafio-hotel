/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */

package br.com.jardel.desafio_hotel.domain.services;

import br.com.jardel.desafio_hotel.api.dtos.PagedResult;
import br.com.jardel.desafio_hotel.api.dtos.PaginacaoRequest;
import br.com.jardel.desafio_hotel.application.dtos.GastoHospedeDTO;
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
public class ConsultarHospedesPresentesUseCaseTest {

    private final FakeHospedeRepository hospedeRepositorio = new FakeHospedeRepository();
    private final FakeCheckInRepository checkInRepositorio = new FakeCheckInRepository(LocalDateTime.of(2026, 2, 7, 12, 0));
    private final ICalculadoraHospedagemService calculadora = new CalculadoraHospedagemService();

    private final IConsultarHospedesPresentesUseCase useCase =
            new ConsultarHospedesPresentesUseCase(hospedeRepositorio, checkInRepositorio, calculadora);

    @Test
    void deveRetornarPagedResultComTotais() {
        Hospede h = hospedeRepositorio.salvar(new Hospede(null, "Fulano", "123", "111"));

        // histórico antigo com total salvo
        checkInRepositorio.salvar(new CheckIn(
                null, h.id(),
                LocalDateTime.of(2026, 2, 2, 10, 0), // segunda
                LocalDateTime.of(2026, 2, 2, 12, 0),
                false,
                new BigDecimal("120.00")
        ));

        // check-in atual presente (exit > agora) com total NULO (força calculadora -> sábado = 150)
        CheckIn atual = checkInRepositorio.salvar(new CheckIn(
                null, h.id(),
                LocalDateTime.of(2026, 2, 7, 10, 0), // sábado
                LocalDateTime.of(2026, 2, 7, 13, 0), // > agora (12:00)
                false,
                null
        ));

        PagedResult<GastoHospedeDTO> result = useCase.execute(new PaginacaoRequest(0, 10));

        assertEquals(1L, result.totalElements());
        assertEquals(1, result.items().size());

        GastoHospedeDTO dto = result.items().get(0);
        assertEquals(h.id(), dto.idHospede());
        assertEquals("Fulano", dto.nome());

        assertEquals(new BigDecimal("270.00"), dto.valorTotalGasto());      // 120 + 150
        assertEquals(new BigDecimal("150.00"), dto.valorUltimaHospedagem()); // sábado

        assertEquals(atual.dataEntrada(), dto.dataEntrada());
        assertEquals(atual.dataSaida(), dto.dataSaida());
    }

    @Test
    void deveFalharSeHospedeDoCheckinNaoExiste() {
        // check-in presente de um hóspede inexistente
        checkInRepositorio.salvar(new CheckIn(
                null, 999L,
                LocalDateTime.of(2026, 2, 7, 10, 0),
                LocalDateTime.of(2026, 2, 7, 13, 0),
                false,
                null
        ));

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> useCase.execute(new PaginacaoRequest(0, 10)));

        assertEquals("Hóspede do check-in não encontrado.", ex.getMessage());
    }
}
