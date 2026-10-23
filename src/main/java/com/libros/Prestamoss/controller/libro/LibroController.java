package com.libros.Prestamoss.controller.libro;

import com.libros.Prestamoss.controller.libro.dto.RequestCreateLibroDto;
import com.libros.Prestamoss.model.libro.Libro;
import com.libros.Prestamoss.service.libro.LibroService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin({"*"})
@RequestMapping("/libros")
public class LibroController {

    private final LibroService libroService;

    public LibroController(LibroService libroService) {
        this.libroService = libroService;
    }

    @PostMapping
    public ResponseEntity<Libro> createLibro(@RequestBody @Valid RequestCreateLibroDto payload) {
        return ResponseEntity
                .status(201)
                .body(libroService.createLibro(payload));
    }
}
