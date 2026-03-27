package com.example.bibliotecaduoc.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

@Data
public class ClientRequest {

    @PositiveOrZero(message = "No puede ser negativo")
    private int id;
    
    @NotBlank(message = "No puede ser vacio")
    private String isbn;
    
    @NotBlank(message = "No puede ser vacio")
    private String titulo;
    
    @NotBlank(message = "No puede ser vacio")
    private String editorial;
    
    @PositiveOrZero(message = "No puede ser negativo")
    private int fechaPublicacion;

    @NotBlank(message = "No puede ser nulo")
    private String autor;

}
