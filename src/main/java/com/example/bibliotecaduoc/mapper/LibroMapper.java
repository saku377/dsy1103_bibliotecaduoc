package com.example.bibliotecaduoc.mapper;

import com.example.bibliotecaduoc.dto.CreateLibroRequest;
import com.example.bibliotecaduoc.dto.UpdateLibroRequest;
import com.example.bibliotecaduoc.model.Libro;

/**
 * Mapper para convertir DTOs a modelo de dominio (Libro) Sigue el patrón de separación de
 * responsabilidades
 */
public class LibroMapper {

    /**
     * Convierte CreateLibroRequest a Libro (para POST) El ID se genera automáticamente, se pasa 0
     * temporalmente
     */
    public static Libro toModel(CreateLibroRequest request) {
        return new Libro(0, // ID temporal, será asignado por el service/repository
                request.isbn(), request.titulo(), request.editorial(), request.fechaPublicacion(),
                request.autor());
    }

    /**
     * Convierte UpdateLibroRequest a Libro (para PUT) El ID se obtiene del path parameter
     */
    public static Libro toModel(int id, UpdateLibroRequest request) {
        return new Libro(id, // ID del path parameter
                request.isbn(), request.titulo(), request.editorial(), request.fechaPublicacion(),
                request.autor());
    }
}
