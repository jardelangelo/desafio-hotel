/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package br.com.jardel.desafio_hotel.application.use_cases;

/**
 *
 * @author jarde
 */

/**
 * Interface genérica para casos de uso.
 * Recebe um Request e retorna um Result.
 */
public interface IUseCase<TRequest, TResult> {
    TResult execute(TRequest request);
}