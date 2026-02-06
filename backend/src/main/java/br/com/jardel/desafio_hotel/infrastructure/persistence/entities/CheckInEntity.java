/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.jardel.desafio_hotel.infrastructure.persistence.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 *
 * @author jarde
 */

@Entity
@Table(name = "check_ins")
public class CheckInEntity {
   
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "guest_id", nullable = false)
    private Long idHospede;

    @Column(name = "entry_at", nullable = false)
    private LocalDateTime dataEntrada;

    @Column(name = "exit_at", nullable = false)
    private LocalDateTime dataSaida;

    @Column(name = "has_vehicle", nullable = false)
    private boolean adicionalVeiculo;
    
    @Column(name = "total_amount", precision = 14, scale = 2)
    private BigDecimal valorTotal;

    protected CheckInEntity() { }

    public CheckInEntity(Long id, Long idHospede, LocalDateTime dataEntrada, LocalDateTime dataSaida, boolean adicionalVeiculo, BigDecimal valorTotal) {
        this.id = id;
        this.idHospede = idHospede;
        this.dataEntrada = dataEntrada;
        this.dataSaida = dataSaida;
        this.adicionalVeiculo = adicionalVeiculo;
        this.valorTotal = valorTotal;
    }

    public Long getId() { return id; }
    public Long getIdHospede() { return idHospede; }
    public LocalDateTime getDataEntrada() { return dataEntrada; }
    public LocalDateTime getDataSaida() { return dataSaida; }
    public boolean isAdicionalVeiculo() { return adicionalVeiculo; }
    public BigDecimal getValorTotal() { return valorTotal; }

    public void setIdHospede(Long idHospede) { this.idHospede = idHospede; }
    public void setDataEntrada(LocalDateTime dataEntrada) { this.dataEntrada = dataEntrada; }
    public void setDataSaida(LocalDateTime dataSaida) { this.dataSaida = dataSaida; }
    public void setAdicionalVeiculo(boolean adicionalVeiculo) { this.adicionalVeiculo = adicionalVeiculo; }
    public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }
    
}
