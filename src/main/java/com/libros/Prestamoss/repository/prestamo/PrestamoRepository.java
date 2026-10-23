package com.libros.Prestamoss.repository.prestamo;

import com.libros.Prestamoss.model.prestamo.EstadoPrestamo;
import com.libros.Prestamoss.model.prestamo.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {

    long countByLectorIdAndEstadoIn(Long lectorId, Collection<EstadoPrestamo> estados);

    List<Prestamo> findByLectorIdAndEstadoIn(Long lectorId, Collection<EstadoPrestamo> estados);
}
