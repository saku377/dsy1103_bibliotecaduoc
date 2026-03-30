package com.example.bibliotecaduoc.controller;

import com.example.bibliotecaduoc.dto.ClientRequest;
import com.example.bibliotecaduoc.exception.ResourceNotFoundException;
import com.example.bibliotecaduoc.mapper.LibroMapper;
import com.example.bibliotecaduoc.model.Libro;
import com.example.bibliotecaduoc.service.LibroService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.validation.BindingResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/libros")
public class LibroController {

    @Autowired
    private LibroService libroService;

    @GetMapping
    public List<Libro> listarLibros() {
        return libroService.getLibros();
    }

    //@PostMapping
    //public Libro agregarLibro(@Valid @RequestBody ClientRequest request) {
    //    return libroService.saveLibro(LibroMapper.toModel(request));
    //}

    @PostMapping
    public ResponseEntity<?> agregarLibro(@Valid @RequestBody ClientRequest request, BindingResult result) {

        if (result.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors().forEach(error -> 
                errores.put(error.getField(), error.getDefaultMessage())
            );
            return ResponseEntity.badRequest().body(errores);
        }

        return ResponseEntity.ok(libroService.saveLibro(LibroMapper.toModel(request)));
    }

    @GetMapping("{id}")
    public ResponseEntity<Libro> buscarLibro(@PathVariable int id){
        Libro libro = libroService.getLibroId(id);
        
        if (libro == null) {
            throw new ResourceNotFoundException("Libro no encontrado para id: " + id);
        }
        
        return ResponseEntity.ok(libro);
    }

    @PutMapping("{id}")
    public Libro actualizarLibro(@PathVariable int id, @RequestBody Libro libro){
        // el id lo usaremos mas adelante
        return libroService.updateLibro(libro);
    }

    @DeleteMapping("{id}")
    public String eliminarLibro(@PathVariable int id) {
        return libroService.deleteLibro(id);
    }


    @GetMapping("/total")
    public int totalLibrosV2() {
        return libroService.totalLibrosV2();
    }
}
