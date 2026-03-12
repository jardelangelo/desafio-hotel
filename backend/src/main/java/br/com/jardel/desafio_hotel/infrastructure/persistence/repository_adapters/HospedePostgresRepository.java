package br.com.jardel.desafio_hotel.infrastructure.persistence.repository_adapters;

import br.com.jardel.desafio_hotel.domain.models.Hospede;
import br.com.jardel.desafio_hotel.domain.repositories.IHospedeRepository;
import br.com.jardel.desafio_hotel.infrastructure.persistence.entities.HospedeEntity;
import br.com.jardel.desafio_hotel.infrastructure.persistence.jpa_repositories.IHospedeJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class HospedePostgresRepository implements IHospedeRepository {

    private final IHospedeJpaRepository jpa;

    public HospedePostgresRepository(IHospedeJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Hospede salvar(Hospede hospede) {
        HospedeEntity ent = new HospedeEntity(
                hospede.id(),
                hospede.nome(),
                hospede.documento(),
                hospede.telefone()
        );
        HospedeEntity salvo = jpa.save(ent);
        return new Hospede(salvo.getId(), salvo.getNome(), salvo.getDocumento(), salvo.getTelefone());
    }

    @Override
    public Optional<Hospede> buscarPorId(Long id) {
        return jpa.findById(id).map(this::toDominio);
    }

    @Override
    public Optional<Hospede> buscarPorDocumento(String documento) {
        return jpa.findByDocumento(documento).map(this::toDominio);
    }

    @Override
    public boolean existePorDocumento(String documento) {
        return jpa.existsByDocumento(documento);
    }

    @Override
    public List<Hospede> listarTodos() {
        return jpa.findAll().stream().map(this::toDominio).toList();
    }

    @Override
    public void excluirPorId(Long id) {
        jpa.deleteById(id);
    }

    @Override
    public List<Hospede> buscarPorTermo(String termo) {
        return jpa.findByNomeContainingIgnoreCaseOrDocumentoContainingOrTelefoneContaining(termo, termo, termo)
                .stream()
                .map(this::toDominio)
                .toList();
    }

    private Hospede toDominio(HospedeEntity e) {
        return new Hospede(e.getId(), e.getNome(), e.getDocumento(), e.getTelefone());
    }
}
