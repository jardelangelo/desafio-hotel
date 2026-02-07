/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */

package br.com.jardel.desafio_hotel.domain.services;

import br.com.jardel.desafio_hotel.api.dtos.BuscarHospedesPorTermoRequest;
import br.com.jardel.desafio_hotel.domain.models.Hospede;
import br.com.jardel.desafio_hotel.test_support.FakeHospedeRepository;
import br.com.jardel.desafio_hotel.application.use_cases.*;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author jarde
 */
public class BuscarHospedesPorTermoUseCaseTest {

    private final FakeHospedeRepository hospedeRepositorio = new FakeHospedeRepository();
    private final IBuscarHospedesPorTermoUseCase useCase = new BuscarHospedesPorTermoUseCase(hospedeRepositorio);

    @Test
    void deveBuscarPorTermoEmNomeDocumentoOuTelefone() {
        hospedeRepositorio.salvar(new Hospede(null, "Fulano de Tal", "123", "1199999"));
        hospedeRepositorio.salvar(new Hospede(null, "Beltrano", "999", "1188888"));

        List<Hospede> lista = useCase.execute(new BuscarHospedesPorTermoRequest("ful"));

        assertEquals(1, lista.size());
        assertEquals("Fulano de Tal", lista.get(0).nome());
    }

    @Test
    void deveFalharQuandoRequestNulo() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> useCase.execute(null));
        assertEquals("Requisição inválida: corpo da requisição (request) é obrigatório.", ex.getMessage());
    }

    @Test
    void deveFalharQuandoTermoObrigatorio() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> useCase.execute(new BuscarHospedesPorTermoRequest(" ")));
        assertEquals("Termo para busca é obrigatório.", ex.getMessage());
    }
}
