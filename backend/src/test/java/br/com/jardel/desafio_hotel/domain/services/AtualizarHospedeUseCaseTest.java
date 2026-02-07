/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */

package br.com.jardel.desafio_hotel.domain.services;

import br.com.jardel.desafio_hotel.api.dtos.AtualizarHospedeRequest;
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
public class AtualizarHospedeUseCaseTest {

    private final FakeHospedeRepository hospedeRepositorio = new FakeHospedeRepository();
    private final IAtualizarHospedeUseCase useCase = new AtualizarHospedeUseCase(hospedeRepositorio);

    @Test
    void deveAtualizarNomeETelefone() {
        Hospede base = hospedeRepositorio.salvar(new Hospede(null, "Fulano", "123", "111"));

        Hospede atualizado = useCase.execute(new AtualizarHospedeRequest(base.id(), "Novo Nome", "222"));

        assertEquals(base.id(), atualizado.id());
        assertEquals("Novo Nome", atualizado.nome());
        assertEquals("123", atualizado.documento());
        assertEquals("222", atualizado.telefone());
    }

    @Test
    void deveManterCamposQuandoNaoInformados() {
        Hospede base = hospedeRepositorio.salvar(new Hospede(null, "Fulano", "123", "111"));

        Hospede atualizado = useCase.execute(new AtualizarHospedeRequest(base.id(), " ", null));

        assertEquals("Fulano", atualizado.nome());
        assertEquals("111", atualizado.telefone());
    }

    @Test
    void deveFalharQuandoIdObrigatorio() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> useCase.execute(new AtualizarHospedeRequest(null, "x", "y")));
        assertEquals("ID do hóspede é obrigatório.", ex.getMessage());
    }

    @Test
    void deveFalharQuandoHospedeNaoExiste() {
        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> useCase.execute(new AtualizarHospedeRequest(999L, "x", "y")));
        assertEquals("Hóspede não encontrado.", ex.getMessage());
    }
}
