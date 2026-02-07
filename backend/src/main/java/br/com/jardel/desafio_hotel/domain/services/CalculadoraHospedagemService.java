/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.jardel.desafio_hotel.domain.services;

import br.com.jardel.desafio_hotel.domain.models.CheckIn;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

/**
 *
 * @author jarde
 */

public class CalculadoraHospedagemService implements ICalculadoraHospedagemService  {
    
    private static final BigDecimal DIARIA_DIA_UTIL = new BigDecimal("120.00");
    private static final BigDecimal DIARIA_FIM_DE_SEMANA = new BigDecimal("150.00");

    private static final BigDecimal GARAGEM_DIA_UTIL = new BigDecimal("15.00");
    private static final BigDecimal GARAGEM_FIM_DE_SEMANA = new BigDecimal("20.00");

    private static final LocalTime HORA_LIMITE_DIARIA_EXTRA = LocalTime.of(16, 30);
    
    @Override
    public BigDecimal calcularTotalHospedagem(CheckIn checkIn) {
        validar(checkIn);

        LocalDate dataEntrada = checkIn.dataEntrada().toLocalDate();
        LocalDate dataSaida = checkIn.dataSaida().toLocalDate();

        long diasBase = ChronoUnit.DAYS.between(dataEntrada, dataSaida);
        long quantidadeDiariasBase = Math.max(1, diasBase);

        BigDecimal total = BigDecimal.ZERO;
        LocalDate dia = dataEntrada;

        // Diárias base
        for (int i = 0; i < quantidadeDiariasBase; i++) {
            total = total.add(valorDiaria(dia, checkIn.adicionalVeiculo()));
            dia = dia.plusDays(1);
        }

        // Diária extra (após 16:30) -> usa o DIA DA SAÍDA
        if (checkIn.dataSaida().toLocalTime().isAfter(HORA_LIMITE_DIARIA_EXTRA)) {
            total = total.add(valorDiaria(dataSaida, checkIn.adicionalVeiculo()));
        }

        return total;
    }

    private BigDecimal valorDiaria(LocalDate dia, boolean adicionalVeiculo) {
        boolean fimDeSemana = ehFimDeSemana(dia);

        BigDecimal valor = fimDeSemana ? DIARIA_FIM_DE_SEMANA : DIARIA_DIA_UTIL;

        if (adicionalVeiculo) {
            valor = valor.add(fimDeSemana ? GARAGEM_FIM_DE_SEMANA : GARAGEM_DIA_UTIL);
        }

        return valor;
    }

    private boolean ehFimDeSemana(LocalDate dia) {
        DayOfWeek dow = dia.getDayOfWeek();
        return dow == DayOfWeek.SATURDAY || dow == DayOfWeek.SUNDAY;
    }

    private void validar(CheckIn checkIn) {
        if (checkIn == null) throw new IllegalArgumentException("Check-in é obrigatório.");
        if (checkIn.dataEntrada() == null) throw new IllegalArgumentException("Data de entrada é obrigatória.");
        if (checkIn.dataSaida() == null) throw new IllegalArgumentException("Data de saída é obrigatória.");
        if (checkIn.dataSaida().isBefore(checkIn.dataEntrada())) {
            throw new IllegalArgumentException("A data de saída do check-in deve ser maior ou igual à data de entrada.");
        }
    }
    
}
