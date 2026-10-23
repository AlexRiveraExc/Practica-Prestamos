package com.libros.Prestamoss.controller.prestamo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RequestSolicitarPrestamoDto {

    @NotNull(message = "El id del lector es obligatorio")
    private Long lectorId;

    @NotNull(message = "El id del libro es obligatorio")
    private Long libroId;
}
