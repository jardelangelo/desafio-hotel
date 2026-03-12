package br.com.jardel.desafio_hotel.application.use_cases;

import br.com.jardel.desafio_hotel.api.dtos.ExcluirHospedeRequest;
import br.com.jardel.desafio_hotel.application.validators.HospedeValidator;
import br.com.jardel.desafio_hotel.domain.repositories.IHospedeRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ExcluirHospedeUseCase implements IExcluirHospedeUseCase {

    private final IHospedeRepository hospedeRepositorio;
    private final HospedeValidator hospedeValidator;

    public ExcluirHospedeUseCase(IHospedeRepository hospedeRepositorio,
                                 HospedeValidator hospedeValidator) {
        this.hospedeRepositorio = hospedeRepositorio;
        this.hospedeValidator = hospedeValidator;
    }

    @Override
    @Transactional
    public Void execute(ExcluirHospedeRequest request) {
        hospedeValidator.validarExclusao(request);
        hospedeRepositorio.excluirPorId(request.id());
        return null;
    }
}
