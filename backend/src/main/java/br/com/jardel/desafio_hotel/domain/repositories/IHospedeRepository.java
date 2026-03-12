package br.com.jardel.desafio_hotel.domain.repositories;

import br.com.jardel.desafio_hotel.domain.models.Hospede;

import java.util.List;
import java.util.Optional;

public interface IHospedeRepository {

    Hospede salvar(Hospede hospede);
    Optional<Hospede> buscarPorId(Long id);
    Optional<Hospede> buscarPorDocumento(String documento);
    boolean existePorDocumento(String documento);
    List<Hospede> listarTodos();
    void excluirPorId(Long id);
    List<Hospede> buscarPorTermo(String termo);
}
