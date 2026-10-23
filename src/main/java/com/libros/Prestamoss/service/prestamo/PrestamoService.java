package com.libros.Prestamoss.service.prestamo;

import com.libros.Prestamoss.controller.prestamo.dto.RequestSolicitarPrestamoDto;
import com.libros.Prestamoss.exception.customExceptions.CustomBadRequestException;
import com.libros.Prestamoss.exception.customExceptions.RecursoNoEncontradoException;
import com.libros.Prestamoss.model.lector.Lector;
import com.libros.Prestamoss.model.libro.Libro;
import com.libros.Prestamoss.model.prestamo.EstadoPrestamo;
import com.libros.Prestamoss.model.prestamo.Prestamo;
import com.libros.Prestamoss.repository.lector.LectorRepository;
import com.libros.Prestamoss.repository.libro.LibroRepository;
import com.libros.Prestamoss.repository.prestamo.PrestamoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class PrestamoService {

    private static final int DIAS_PRESTAMO = 14;
    private static final int DIAS_RENOVACION = 7;
    private static final int LIMITE_PRESTAMOS = 3;
    private static final int LIMITE_PRESTAMOS_PREMIUM = 5;
    private static final List<EstadoPrestamo> ESTADOS_ACTIVOS = List.of(EstadoPrestamo.PRESTADO, EstadoPrestamo.VENCIDO);

    private final PrestamoRepository prestamoRepository;
    private final LectorRepository lectorRepository;
    private final LibroRepository libroRepository;

    public PrestamoService(PrestamoRepository prestamoRepository,
                           LectorRepository lectorRepository,
                           LibroRepository libroRepository) {
        this.prestamoRepository = prestamoRepository;
        this.lectorRepository = lectorRepository;
        this.libroRepository = libroRepository;
    }

    @Transactional
    public Prestamo solicitarPrestamo(RequestSolicitarPrestamoDto payload) {
        Lector lector = buscarLector(payload.getLectorId());
        Libro libro = buscarLibro(payload.getLibroId());

        if (!lector.isActivo()) {
            throw new CustomBadRequestException("El lector esta bloqueado y no puede solicitar prestamos");
        }

        if (libro.getEjemplaresDisponibles() < 1) {
            throw new CustomBadRequestException("No hay ejemplares disponibles del libro");
        }

        long prestamosActivos = prestamoRepository.countByLectorIdAndEstadoIn(lector.getId(), ESTADOS_ACTIVOS);
        int limite = lector.isMembresiaPremium() ? LIMITE_PRESTAMOS_PREMIUM : LIMITE_PRESTAMOS;

        if (prestamosActivos >= limite) {
            throw new CustomBadRequestException("El lector ya tiene " + prestamosActivos
                    + " prestamos activos y el limite es " + limite);
        }

        libro.setEjemplaresDisponibles(libro.getEjemplaresDisponibles() - 1);
        libroRepository.save(libro);

        LocalDate hoy = LocalDate.now();
        Prestamo prestamo = new Prestamo();
        prestamo.setLector(lector);
        prestamo.setLibro(libro);
        prestamo.setFechaPrestamo(hoy);
        prestamo.setFechaDevolucionMaxima(hoy.plusDays(DIAS_PRESTAMO));
        prestamo.setEstado(EstadoPrestamo.PRESTADO);

        return prestamoRepository.save(prestamo);
    }

    @Transactional
    public Prestamo devolverLibro(Long prestamoId) {
        Prestamo prestamo = buscarPrestamo(prestamoId);

        if (prestamo.getEstado() != EstadoPrestamo.PRESTADO && prestamo.getEstado() != EstadoPrestamo.VENCIDO) {
            throw new CustomBadRequestException("Solo se pueden devolver prestamos en estado PRESTADO o VENCIDO");
        }

        if (LocalDate.now().isAfter(prestamo.getFechaDevolucionMaxima())) {
            Lector lector = prestamo.getLector();
            lector.setActivo(false);
            lectorRepository.save(lector);
        }

        Libro libro = prestamo.getLibro();
        libro.setEjemplaresDisponibles(libro.getEjemplaresDisponibles() + 1);
        libroRepository.save(libro);

        prestamo.setEstado(EstadoPrestamo.DEVUELTO);
        return prestamoRepository.save(prestamo);
    }

    @Transactional
    public Prestamo renovarPrestamo(Long prestamoId) {
        Prestamo prestamo = buscarPrestamo(prestamoId);

        if (prestamo.getEstado() == EstadoPrestamo.VENCIDO || prestamo.getEstado() == EstadoPrestamo.DEVUELTO) {
            throw new CustomBadRequestException("Solo se pueden renovar prestamos en estado PRESTADO");
        }

        prestamo.setFechaDevolucionMaxima(prestamo.getFechaDevolucionMaxima().plusDays(DIAS_RENOVACION));
        return prestamoRepository.save(prestamo);
    }

    public List<Prestamo> listarPrestamosActivosLector(Long lectorId) {
        buscarLector(lectorId);
        return prestamoRepository.findByLectorIdAndEstadoIn(lectorId, ESTADOS_ACTIVOS);
    }

    private Prestamo buscarPrestamo(Long id) {
        return prestamoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("El prestamo con id " + id + " no existe"));
    }

    private Lector buscarLector(Long id) {
        return lectorRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("El lector con id " + id + " no existe"));
    }

    private Libro buscarLibro(Long id) {
        return libroRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("El libro con id " + id + " no existe"));
    }
}
