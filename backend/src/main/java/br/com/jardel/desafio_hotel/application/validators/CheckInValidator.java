package br.com.jardel.desafio_hotel.application.validators;

import br.com.jardel.desafio_hotel.api.dtos.AtualizarCheckInRequest;
import br.com.jardel.desafio_hotel.api.dtos.RealizarCheckInRequest;
import br.com.jardel.desafio_hotel.api.exceptions.ConflictException;
import br.com.jardel.desafio_hotel.api.exceptions.NotFoundException;
import br.com.jardel.desafio_hotel.domain.models.CheckIn;
import br.com.jardel.desafio_hotel.domain.repositories.ICheckInRepository;
import br.com.jardel.desafio_hotel.domain.repositories.IHospedeRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CheckInValidator {

    private final IHospedeRepository hospedeRepositorio;
    private final ICheckInRepository checkInRepositorio;

    public CheckInValidator(IHospedeRepository hospedeRepositorio,
                            ICheckInRepository checkInRepositorio) {
        this.hospedeRepositorio = hospedeRepositorio;
        this.checkInRepositorio = checkInRepositorio;
    }

    public void validarCadastro(RealizarCheckInRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Requisição inválida: corpo da requisição (request) é obrigatório.");
        }
        if (request.idHospede() == null) {
            throw new IllegalArgumentException("Hóspede é obrigatório.");
        }
        if (request.dataEntrada() == null) {
            throw new IllegalArgumentException("Data de entrada para CheckIn é obrigatória.");
        }
        if (request.dataSaida() == null) {
            throw new IllegalArgumentException("Data de saída para CheckIn é obrigatória.");
        }

        validarPeriodo(request.dataEntrada(), request.dataSaida());
        validarHospedeExistente(request.idHospede());

        if (checkInRepositorio.existeSobreposicao(request.idHospede(), request.dataEntrada(), request.dataSaida())) {
            throw new ConflictException("O hóspede já possui uma hospedagem no período informado.");
        }
    }

    public void validarAtualizacao(AtualizarCheckInRequest request, CheckIn checkInAtual) {
        if (request == null || request.id() == null) {
            throw new IllegalArgumentException("ID do check-in é obrigatório.");
        }

        LocalDateTime novaEntrada = request.dataEntrada() != null ? request.dataEntrada() : checkInAtual.dataEntrada();
        LocalDateTime novaSaida = request.dataSaida() != null ? request.dataSaida() : checkInAtual.dataSaida();

        validarPeriodo(novaEntrada, novaSaida);

        if (checkInRepositorio.existeSobreposicaoExcluindoId(checkInAtual.idHospede(), checkInAtual.id(), novaEntrada, novaSaida)) {
            throw new ConflictException("O hóspede já possui uma hospedagem no período informado.");
        }
    }

    public void validarAtualizacao(AtualizarCheckInRequest request) {
        if (request == null || request.id() == null) {
            throw new IllegalArgumentException("ID do check-in é obrigatório.");
        }
    }

    private void validarPeriodo(LocalDateTime dataEntrada, LocalDateTime dataSaida) {
        if (dataSaida.isBefore(dataEntrada)) {
            throw new IllegalArgumentException("A data de saída do check-in deve ser maior ou igual à data de entrada.");
        }

        if (dataEntrada.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Não é permitido agendar check-in futuro. Por isso, a data de entrada deve ser hoje ou uma data anterior.");
        }
    }

    private void validarHospedeExistente(Long idHospede) {
        hospedeRepositorio.buscarPorId(idHospede)
                .orElseThrow(() -> new NotFoundException("Hóspede não encontrado."));
    }
}
