/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.jardel.desafio_hotel.api.dto;

/**
 *
 * @author jarde
 */
public record HospedeResponse(
        Long id,
        String nome,
        String documento,
        String telefone
) {}
