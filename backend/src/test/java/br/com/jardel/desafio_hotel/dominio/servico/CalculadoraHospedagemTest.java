/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package br.com.jardel.desafio_hotel.dominio.servico;

import br.com.jardel.desafio_hotel.dominio.servico.CalculadoraHospedagem;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import br.com.jardel.desafio_hotel.dominio.modelo.CheckIn;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 *
 * @author jarde
 */
public class CalculadoraHospedagemTest {
    
    private final CalculadoraHospedagem calculadora = new CalculadoraHospedagem();

    @Test
    void deveCobrarUmaDiariaDiaUtilSemVeiculo() {
        CheckIn checkIn = new CheckIn(
                null, 1L,
                LocalDateTime.of(2026, 2, 2, 10, 0),  // segunda
                LocalDateTime.of(2026, 2, 2, 12, 0),
                false
        );

        BigDecimal total = calculadora.calcularTotalHospedagem(checkIn);
        assertEquals(new BigDecimal("120.00"), total);
    }

    @Test
    void deveCobrarDiariaExtraQuandoSaidaApos1630() {
        CheckIn checkIn = new CheckIn(
                null, 1L,
                LocalDateTime.of(2026, 2, 2, 10, 0),  // segunda
                LocalDateTime.of(2026, 2, 2, 17, 0),  // após 16:30
                false
        );

        BigDecimal total = calculadora.calcularTotalHospedagem(checkIn);
        assertEquals(new BigDecimal("240.00"), total);
    }

    @Test
    void deveCobrarFimDeSemanaComVeiculo() {
        CheckIn checkIn = new CheckIn(
                null, 1L,
                LocalDateTime.of(2026, 2, 7, 10, 0),  // sábado
                LocalDateTime.of(2026, 2, 7, 12, 0),
                true
        );

        BigDecimal total = calculadora.calcularTotalHospedagem(checkIn);
        assertEquals(new BigDecimal("170.00"), total); // 150 + 20
    }
    
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
