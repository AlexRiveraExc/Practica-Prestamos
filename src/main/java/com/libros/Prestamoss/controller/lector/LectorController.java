package com.libros.Prestamoss.controller.lector;

import com.libros.Prestamoss.controller.lector.dto.RequestCreateLectorDto;
import com.libros.Prestamoss.model.lector.Lector;
import com.libros.Prestamoss.service.lector.LectorService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin({"*"})
@RequestMapping("/lectores")
public class LectorController {

    private final LectorService lectorService;

    public LectorController(LectorService lectorService) {
        this.lectorService = lectorService;
    }

    @PostMapping
    public ResponseEntity<Lector> createLector(@RequestBody @Valid RequestCreateLectorDto payload) {
        return ResponseEntity
                .status(201)
                .body(lectorService.createLector(payload));
    }

    @PutMapping("/{id}/rehabilitacion")
    public ResponseEntity<Lector> rehabilitarLector(@PathVariable Long id) {
        return ResponseEntity
                .status(200)
                .body(lectorService.rehabilitarLector(id));
    }
}
