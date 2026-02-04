package br.com.jardel.desafio_hotel.dominio.modelo;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author jarde
 */
public record Hospede(
        Long id,
        String nome,
        String documento,
        String telefone
) {}
