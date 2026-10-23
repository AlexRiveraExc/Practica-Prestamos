package com.libros.Prestamoss.controller.prestamo;

import com.libros.Prestamoss.controller.prestamo.dto.RequestSolicitarPrestamoDto;
import com.libros.Prestamoss.model.prestamo.Prestamo;
import com.libros.Prestamoss.service.prestamo.PrestamoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin({"*"})
@RequestMapping("/prestamos")
public class PrestamoController {

    private final PrestamoService prestamoService;

    public PrestamoController(PrestamoService prestamoService) {
        this.prestamoService = prestamoService;
    }

    @PostMapping
    public ResponseEntity<Prestamo> solicitarPrestamo(@RequestBody @Valid RequestSolicitarPrestamoDto payload) {
        return ResponseEntity
                .status(201)
                .body(prestamoService.solicitarPrestamo(payload));
    }

    @PutMapping("/{id}/devolucion")
    public ResponseEntity<Prestamo> devolverLibro(@PathVariable Long id) {
        return ResponseEntity
                .status(200)
                .body(prestamoService.devolverLibro(id));
    }

    @PutMapping("/{id}/renovacion")
    public ResponseEntity<Prestamo> renovarPrestamo(@PathVariable Long id) {
        return ResponseEntity
                .status(200)
                .body(prestamoService.renovarPrestamo(id));
    }

    @GetMapping("/lector/{lectorId}/activos")
    public ResponseEntity<List<Prestamo>> listarPrestamosActivosLector(@PathVariable Long lectorId) {
        return ResponseEntity
                .status(200)
                .body(prestamoService.listarPrestamosActivosLector(lectorId));
    }
}
