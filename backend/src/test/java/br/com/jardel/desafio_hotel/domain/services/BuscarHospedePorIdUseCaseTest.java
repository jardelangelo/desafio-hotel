/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */

package br.com.jardel.desafio_hotel.domain.services;

import br.com.jardel.desafio_hotel.api.dtos.BuscarHospedePorIdRequest;
import br.com.jardel.desafio_hotel.api.exceptions.NotFoundException;
import br.com.jardel.desafio_hotel.domain.models.Hospede;
import br.com.jardel.desafio_hotel.test_support.FakeHospedeRepository;
import br.com.jardel.desafio_hotel.application.use_cases.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author jarde
 */
public class BuscarHospedePorIdUseCaseTest {

    private final FakeHospedeRepository hospedeRepositorio = new FakeHospedeRepository();
    private final IBuscarHospedePorIdUseCase useCase = new BuscarHospedePorIdUseCase(hospedeRepositorio);

    @Test
    void deveRetornarHospedeQuandoExiste() {
        Hospede base = hospedeRepositorio.salvar(new Hospede(null, "Fulano", "123", "111"));

        Hospede resp = useCase.execute(new BuscarHospedePorIdRequest(base.id()));

        assertEquals(base.id(), resp.id());
        assertEquals("Fulano", resp.nome());
    }

    @Test
    void deveFalharQuandoIdObrigatorio() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> useCase.execute(new BuscarHospedePorIdRequest(null)));
        assertEquals("ID do hóspede é obrigatório.", ex.getMessage());
    }

    @Test
    void deveFalharQuandoNaoEncontrado() {
        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> useCase.execute(new BuscarHospedePorIdRequest(999L)));
        assertEquals("Hóspede não encontrado.", ex.getMessage());
    }
}
