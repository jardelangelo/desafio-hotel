/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.jardel.desafio_hotel.infraestrutura.persistencia.entidade;

import jakarta.persistence.*;

/**
 *
 * @author jarde
 */

@Entity
@Table(name = "guests")
public class HospedeEntidade {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 200)
    private String nome;

    @Column(name = "document", nullable = false, unique = true, length = 50)
    private String documento;

    @Column(name = "phone", nullable = false, length = 30)
    private String telefone;

    protected HospedeEntidade() { }

    public HospedeEntidade(Long id, String nome, String documento, String telefone) {
        this.id = id;
        this.nome = nome;
        this.documento = documento;
        this.telefone = telefone;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getDocumento() { return documento; }
    public String getTelefone() { return telefone; }

    public void setNome(String nome) { this.nome = nome; }
    public void setDocumento(String documento) { this.documento = documento; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    
}
