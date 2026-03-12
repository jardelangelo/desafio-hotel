package br.com.jardel.desafio_hotel.application.use_cases;

import br.com.jardel.desafio_hotel.api.dtos.BuscarHospedePorIdRequest;
import br.com.jardel.desafio_hotel.api.exceptions.NotFoundException;
import br.com.jardel.desafio_hotel.domain.models.Hospede;
import br.com.jardel.desafio_hotel.domain.repositories.IHospedeRepository;
import org.springframework.stereotype.Component;

@Component
public class BuscarHospedePorIdUseCase implements IBuscarHospedePorIdUseCase {

    private final IHospedeRepository hospedeRepositorio;

    public BuscarHospedePorIdUseCase(IHospedeRepository hospedeRepositorio) {
        this.hospedeRepositorio = hospedeRepositorio;
    }

    @Override
    public Hospede execute(BuscarHospedePorIdRequest request) {
        if (request == null || request.id() == null) throw new IllegalArgumentException("ID do hóspede é obrigatório.");

        return hospedeRepositorio.buscarPorId(request.id())
                .orElseThrow(() -> new NotFoundException("Hóspede não encontrado."));
    }
}
