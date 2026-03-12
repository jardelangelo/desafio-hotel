package br.com.jardel.desafio_hotel.application.validators;

import br.com.jardel.desafio_hotel.api.dtos.AtualizarHospedeRequest;
import br.com.jardel.desafio_hotel.api.dtos.CadastrarHospedeRequest;
import br.com.jardel.desafio_hotel.api.dtos.ExcluirHospedeRequest;
import br.com.jardel.desafio_hotel.api.exceptions.ConflictException;
import br.com.jardel.desafio_hotel.api.exceptions.NotFoundException;
import br.com.jardel.desafio_hotel.domain.repositories.ICheckInRepository;
import br.com.jardel.desafio_hotel.domain.repositories.IHospedeRepository;
import org.springframework.stereotype.Component;

@Component
public class HospedeValidator {

    private final IHospedeRepository hospedeRepositorio;
    private final ICheckInRepository checkInRepositorio;

    public HospedeValidator(IHospedeRepository hospedeRepositorio,
                            ICheckInRepository checkInRepositorio) {
        this.hospedeRepositorio = hospedeRepositorio;
        this.checkInRepositorio = checkInRepositorio;
    }

    public void validarCadastro(CadastrarHospedeRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Requisição inválida: corpo da requisição (request) é obrigatório.");
        }

        validarNomeObrigatorio(request.nome());
        validarDocumentoObrigatorio(request.documento());
        validarTelefoneObrigatorio(request.telefone());

        if (hospedeRepositorio.existePorDocumento(request.documento().trim())) {
            throw new IllegalArgumentException("Já existe um hóspede cadastrado com este documento.");
        }
    }

    public void validarAtualizacao(AtualizarHospedeRequest request) {
        if (request == null || request.id() == null) {
            throw new IllegalArgumentException("ID do hóspede é obrigatório.");
        }
    }

    public void validarExclusao(ExcluirHospedeRequest request) {
        if (request == null || request.id() == null) {
            throw new IllegalArgumentException("ID do hóspede é obrigatório.");
        }
           
        if (hospedeRepositorio.buscarPorId(request.id()).isEmpty()) {
            throw new NotFoundException("Hóspede não encontrado.");
        }
        
        if (checkInRepositorio.existePorHospede(request.id())) {
            throw new ConflictException("Não é possível excluir o hóspede, pois existem check-ins vinculados a ele.");
        }
    }

    private void validarNomeObrigatorio(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome do hóspede é obrigatório.");
        }
    }

    private void validarDocumentoObrigatorio(String documento) {
        if (documento == null || documento.isBlank()) {
            throw new IllegalArgumentException("Documento do hóspede é obrigatório.");
        }
    }

    private void validarTelefoneObrigatorio(String telefone) {
        if (telefone == null || telefone.isBlank()) {
            throw new IllegalArgumentException("Telefone do hóspede é obrigatório.");
        }
    }
}
