package com.libros.Prestamoss.service.lector;

import com.libros.Prestamoss.controller.lector.dto.RequestCreateLectorDto;
import com.libros.Prestamoss.exception.customExceptions.RecursoNoEncontradoException;
import com.libros.Prestamoss.model.lector.Lector;
import com.libros.Prestamoss.repository.lector.LectorRepository;
import org.springframework.stereotype.Service;

@Service
public class LectorService {

    private final LectorRepository lectorRepository;

    public LectorService(LectorRepository lectorRepository) {
        this.lectorRepository = lectorRepository;
    }

    public Lector createLector(RequestCreateLectorDto payload) {
        Lector lector = new Lector();
        lector.setNombre(payload.getNombre());
        lector.setActivo(true);
        lector.setMembresiaPremium(payload.isMembresiaPremium());
        return lectorRepository.save(lector);
    }

    public Lector rehabilitarLector(Long id) {
        Lector lector = buscarPorId(id);
        lector.setActivo(true);
        return lectorRepository.save(lector);
    }

    private Lector buscarPorId(Long id) {
        return lectorRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("El lector con id " + id + " no existe"));
    }
}
