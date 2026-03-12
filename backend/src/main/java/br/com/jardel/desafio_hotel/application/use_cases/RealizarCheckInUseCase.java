package br.com.jardel.desafio_hotel.application.use_cases;

import br.com.jardel.desafio_hotel.api.dtos.RealizarCheckInRequest;
import br.com.jardel.desafio_hotel.application.validators.CheckInValidator;
import br.com.jardel.desafio_hotel.domain.models.CheckIn;
import br.com.jardel.desafio_hotel.domain.repositories.ICheckInRepository;
import br.com.jardel.desafio_hotel.domain.services.ICalculadoraHospedagemService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Component
public class RealizarCheckInUseCase implements IRealizarCheckInUseCase {

    private final ICheckInRepository checkInRepositorio;
    private final ICalculadoraHospedagemService calculadora;
    private final CheckInValidator checkInValidator;

    public RealizarCheckInUseCase(ICheckInRepository checkInRepositorio,
                                  ICalculadoraHospedagemService calculadora,
                                  CheckInValidator checkInValidator) {
        this.checkInRepositorio = checkInRepositorio;
        this.calculadora = calculadora;
        this.checkInValidator = checkInValidator;
    }

    @Override
    @Transactional
    public CheckIn execute(RealizarCheckInRequest request) {
        checkInValidator.validarCadastro(request);

        CheckIn paraCalcular = new CheckIn(null, request.idHospede(), request.dataEntrada(), request.dataSaida(),
                request.adicionalVeiculo(), null);

        BigDecimal valorTotal = calculadora.calcularTotalHospedagem(paraCalcular);

        CheckIn novo = new CheckIn(null, request.idHospede(), request.dataEntrada(), request.dataSaida(),
                request.adicionalVeiculo(), valorTotal);

        return checkInRepositorio.salvar(novo);
    }
}
