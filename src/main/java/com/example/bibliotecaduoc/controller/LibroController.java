package com.example.bibliotecaduoc.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.bibliotecaduoc.dto.CreateLibroRequest;
import com.example.bibliotecaduoc.dto.UpdateLibroRequest;
import com.example.bibliotecaduoc.exception.ResourceNotFoundException;
import com.example.bibliotecaduoc.mapper.LibroMapper;
import com.example.bibliotecaduoc.model.Libro;
import com.example.bibliotecaduoc.service.LibroService;
import jakarta.validation.Valid;

/**
 * Controller REST modernizado para Java 21 LTS y Spring Boot 3.3+ 100% REST compliant
 */
@RestController
@RequestMapping("/api/v1/libros")
public class LibroController {

        private final LibroService libroService;

        // Constructor injection (mejor práctica 2026)
        public LibroController(LibroService libroService) {
                this.libroService = libroService;
        }

        @GetMapping
        public ResponseEntity<List<Libro>> listarLibros() {
                List<Libro> libros = libroService.getLibros();
                return ResponseEntity.ok(libros);
        }

        @PostMapping
        public ResponseEntity<Libro> agregarLibro(@Valid @RequestBody CreateLibroRequest request) {
                // @Valid ejecuta validaciones Jakarta automáticamente
                // Si falla → GlobalExceptionHandler.handleValidationErrors() retorna 400

                Libro nuevoLibro = libroService.saveLibro(LibroMapper.toModel(request));
                return ResponseEntity.status(HttpStatus.CREATED).body(nuevoLibro);
        }

        @GetMapping("{id}")
        public ResponseEntity<Libro> buscarLibro(@PathVariable int id) {
                Libro libro = libroService.getLibroId(id);

                if (libro == null) {

                        throw new ResourceNotFoundException("Libro no encontrado para id: " + id);
                }

                return ResponseEntity.ok(libro);
        }

        @PutMapping("{id}")
        public ResponseEntity<Libro> actualizarLibro(@PathVariable int id,
                        @Valid @RequestBody UpdateLibroRequest request) {
                // El ID viene del path, no del body → evita ambigüedad
                Libro libroActualizado = libroService.updateLibro(LibroMapper.toModel(id, request));
                return ResponseEntity.ok(libroActualizado);
        }

        @DeleteMapping("{id}")
        public ResponseEntity<Void> eliminarLibro(@PathVariable int id) {
                libroService.deleteLibro(id);
                return ResponseEntity.noContent().build(); // 204 No Content (estándar REST)
        }

        @GetMapping("/total")
        public ResponseEntity<Integer> totalLibros() {
                int total = libroService.totalLibrosV2();
                return ResponseEntity.ok(total);
        }
}
