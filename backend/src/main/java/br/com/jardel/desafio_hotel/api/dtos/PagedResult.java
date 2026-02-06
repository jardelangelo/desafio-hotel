/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.jardel.desafio_hotel.api.dtos;

import java.util.List;

/**
 *
 * @author jarde
 */

public record PagedResult<T>(
        List<T> items,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean hasNext,
        boolean hasPrevious
) {
    public static <T> PagedResult<T> of(List<T> items, int page, int size, long totalElements) {
        int totalPages = (int) Math.ceil((double) totalElements / (double) size);
        boolean hasPrevious = page > 0;
        boolean hasNext = (page + 1) < totalPages;
        return new PagedResult<>(items, page, size, totalElements, totalPages, hasNext, hasPrevious);
    }
}
