/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */

package br.com.jardel.desafio_hotel.domain.services;

import br.com.jardel.desafio_hotel.api.dtos.CadastrarHospedeRequest;
import br.com.jardel.desafio_hotel.domain.models.Hospede;
import br.com.jardel.desafio_hotel.test_support.FakeHospedeRepository;
import br.com.jardel.desafio_hotel.application.use_cases.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author jarde
 */
public class CadastrarHospedeUseCaseTest {

    private final FakeHospedeRepository hospedeRepositorio = new FakeHospedeRepository();
    private final ICadastrarHospedeUseCase useCase = new CadastrarHospedeUseCase(hospedeRepositorio);

    @Test
    void deveCadastrarHospedeQuandoRequestValido() {
        CadastrarHospedeRequest req = new CadastrarHospedeRequest("Fulano", "123", "1199999");

        Hospede salvo = useCase.execute(req);

        assertNotNull(salvo);
        assertNotNull(salvo.id());
        assertEquals("Fulano", salvo.nome());
        assertEquals("123", salvo.documento());
        assertEquals("1199999", salvo.telefone());
    }

    @Test
    void deveFalharQuandoDocumentoJaExiste() {
        useCase.execute(new CadastrarHospedeRequest("Fulano", "123", "1199999"));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                useCase.execute(new CadastrarHospedeRequest("Outro", "123", "111"))
        );

        assertEquals("Já existe um hóspede cadastrado com este documento.", ex.getMessage());
    }

    @Test
    void deveFalharQuandoRequestNulo() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> useCase.execute(null));
        assertEquals("Requisição inválida: corpo da requisição (request) é obrigatório.", ex.getMessage());
    }

    @Test
    void deveFalharQuandoCamposObrigatoriosVazios() {
        assertEquals("Nome do hóspede é obrigatório.",
                assertThrows(IllegalArgumentException.class,
                        () -> useCase.execute(new CadastrarHospedeRequest(" ", "123", "1"))).getMessage());

        assertEquals("Documento do hóspede é obrigatório.",
                assertThrows(IllegalArgumentException.class,
                        () -> useCase.execute(new CadastrarHospedeRequest("Fulano", " ", "1"))).getMessage());

        assertEquals("Telefone do hóspede é obrigatório.",
                assertThrows(IllegalArgumentException.class,
                        () -> useCase.execute(new CadastrarHospedeRequest("Fulano", "123", " "))).getMessage());
    }
}
