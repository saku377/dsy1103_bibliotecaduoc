package com.example.bibliotecaduoc.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * DTO para actualizar un libro existente (PUT) No incluye ID porque se obtiene del path parameter
 */
public record UpdateLibroRequest(@NotBlank(message = "ISBN no puede ser vacío") String isbn,

                @NotBlank(message = "Título no puede ser vacío") String titulo,

                @NotBlank(message = "Editorial no puede ser vacía") String editorial,

                @PositiveOrZero(message = "Año de publicación no puede ser negativo") int fechaPublicacion,

                @NotBlank(message = "Autor no puede ser vacío") String autor) {
}
