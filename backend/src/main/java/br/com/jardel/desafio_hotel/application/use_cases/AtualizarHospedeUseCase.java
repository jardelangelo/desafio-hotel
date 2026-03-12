package br.com.jardel.desafio_hotel.application.use_cases;

import br.com.jardel.desafio_hotel.api.dtos.AtualizarHospedeRequest;
import br.com.jardel.desafio_hotel.api.exceptions.NotFoundException;
import br.com.jardel.desafio_hotel.application.validators.HospedeValidator;
import br.com.jardel.desafio_hotel.domain.models.Hospede;
import br.com.jardel.desafio_hotel.domain.repositories.IHospedeRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AtualizarHospedeUseCase implements IAtualizarHospedeUseCase {

    private final IHospedeRepository hospedeRepositorio;
    private final HospedeValidator hospedeValidator;

    public AtualizarHospedeUseCase(IHospedeRepository hospedeRepositorio,
                                   HospedeValidator hospedeValidator) {
        this.hospedeRepositorio = hospedeRepositorio;
        this.hospedeValidator = hospedeValidator;
    }

    @Override
    @Transactional
    public Hospede execute(AtualizarHospedeRequest request) {
        hospedeValidator.validarAtualizacao(request);

        Hospede atual = hospedeRepositorio.buscarPorId(request.id())
                .orElseThrow(() -> new NotFoundException("Hóspede não encontrado."));

        String nome = (request.nome() == null || request.nome().isBlank()) ? atual.nome() : request.nome().trim();
        String telefone = (request.telefone() == null || request.telefone().isBlank()) ? atual.telefone() : request.telefone().trim();

        Hospede atualizado = new Hospede(atual.id(), nome, atual.documento(), telefone);
        return hospedeRepositorio.salvar(atualizado);
    }
}
