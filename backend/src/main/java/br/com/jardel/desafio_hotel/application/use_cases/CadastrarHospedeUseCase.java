package br.com.jardel.desafio_hotel.application.use_cases;

import br.com.jardel.desafio_hotel.api.dtos.CadastrarHospedeRequest;
import br.com.jardel.desafio_hotel.application.validators.HospedeValidator;
import br.com.jardel.desafio_hotel.domain.models.Hospede;
import br.com.jardel.desafio_hotel.domain.repositories.IHospedeRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CadastrarHospedeUseCase implements ICadastrarHospedeUseCase {

    private final IHospedeRepository hospedeRepositorio;
    private final HospedeValidator hospedeValidator;

    public CadastrarHospedeUseCase(IHospedeRepository hospedeRepositorio,
                                   HospedeValidator hospedeValidator) {
        this.hospedeRepositorio = hospedeRepositorio;
        this.hospedeValidator = hospedeValidator;
    }

    @Override
    @Transactional
    public Hospede execute(CadastrarHospedeRequest request) {
        hospedeValidator.validarCadastro(request);

        Hospede novo = new Hospede(null, request.nome().trim(), request.documento().trim(), request.telefone().trim());
        return hospedeRepositorio.salvar(novo);
    }
}
