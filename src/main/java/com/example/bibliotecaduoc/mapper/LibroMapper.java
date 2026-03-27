package com.example.bibliotecaduoc.mapper;

import com.example.bibliotecaduoc.dto.ClientRequest;
import com.example.bibliotecaduoc.model.Libro;

public class LibroMapper {

    public static Libro toModel(ClientRequest request) {
        return new Libro(
            request.getId(),
            request.getIsbn(),
            request.getTitulo(),
            request.getEditorial(),
            request.getFechaPublicacion(),
            request.getAutor()
        );
    }
}