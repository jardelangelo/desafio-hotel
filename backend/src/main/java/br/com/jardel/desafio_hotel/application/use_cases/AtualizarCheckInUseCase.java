package br.com.jardel.desafio_hotel.application.use_cases;

import br.com.jardel.desafio_hotel.api.dtos.AtualizarCheckInRequest;
import br.com.jardel.desafio_hotel.api.exceptions.NotFoundException;
import br.com.jardel.desafio_hotel.application.validators.CheckInValidator;
import br.com.jardel.desafio_hotel.domain.models.CheckIn;
import br.com.jardel.desafio_hotel.domain.repositories.ICheckInRepository;
import br.com.jardel.desafio_hotel.domain.services.ICalculadoraHospedagemService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class AtualizarCheckInUseCase implements IAtualizarCheckInUseCase {

    private final ICheckInRepository checkInRepositorio;
    private final ICalculadoraHospedagemService calculadora;
    private final CheckInValidator checkInValidator;

    public AtualizarCheckInUseCase(ICheckInRepository checkInRepositorio,
                                   ICalculadoraHospedagemService calculadora,
                                   CheckInValidator checkInValidator) {
        this.checkInRepositorio = checkInRepositorio;
        this.calculadora = calculadora;
        this.checkInValidator = checkInValidator;
    }

    @Override
    @Transactional
    public CheckIn execute(AtualizarCheckInRequest request) {
        checkInValidator.validarAtualizacao(request);

        CheckIn atual = checkInRepositorio.buscarPorId(request.id())
                .orElseThrow(() -> new NotFoundException("Check-in não encontrado."));

        checkInValidator.validarAtualizacao(request, atual);

        LocalDateTime novaEntrada = request.dataEntrada() != null ? request.dataEntrada() : atual.dataEntrada();
        LocalDateTime novaSaida = request.dataSaida() != null ? request.dataSaida() : atual.dataSaida();
        boolean novoVeiculo = request.adicionalVeiculo() != null ? request.adicionalVeiculo() : atual.adicionalVeiculo();

        CheckIn paraCalcular = new CheckIn(atual.id(), atual.idHospede(), novaEntrada, novaSaida, novoVeiculo, null);
        BigDecimal novoTotal = calculadora.calcularTotalHospedagem(paraCalcular);

        CheckIn atualizado = new CheckIn(atual.id(), atual.idHospede(), novaEntrada, novaSaida, novoVeiculo, novoTotal);
        return checkInRepositorio.salvar(atualizado);
    }
}
