/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */

package br.com.jardel.desafio_hotel.domain.services;

import br.com.jardel.desafio_hotel.api.dtos.EmptyRequest;
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
public class ListarHospedesUseCaseTest {

    private final FakeHospedeRepository hospedeRepositorio = new FakeHospedeRepository();
    private final IListarHospedesUseCase useCase = new ListarHospedesUseCase(hospedeRepositorio);

    @Test
    void deveListarTodos() {
        hospedeRepositorio.salvar(new Hospede(null, "Fulano", "123", "111"));
        hospedeRepositorio.salvar(new Hospede(null, "Beltrano", "456", "222"));

        List<Hospede> lista = useCase.execute(new EmptyRequest());

        assertEquals(2, lista.size());
    }
}
