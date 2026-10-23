package com.libros.Prestamoss.service.libro;

import com.libros.Prestamoss.controller.libro.dto.RequestCreateLibroDto;
import com.libros.Prestamoss.model.libro.Libro;
import com.libros.Prestamoss.repository.libro.LibroRepository;
import org.springframework.stereotype.Service;

@Service
public class LibroService {

    private final LibroRepository libroRepository;

    public LibroService(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }

    public Libro createLibro(RequestCreateLibroDto payload) {
        Libro libro = new Libro();
        libro.setTitulo(payload.getTitulo());
        libro.setEjemplaresDisponibles(payload.getEjemplaresDisponibles());
        return libroRepository.save(libro);
    }
}
