package com.libros.Prestamoss.controller.libro.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RequestCreateLibroDto {

    @NotBlank(message = "El titulo del libro es obligatorio")
    private String titulo;

    @NotNull(message = "La cantidad de ejemplares es obligatoria")
    @Min(value = 0, message = "Los ejemplares disponibles no pueden ser negativos")
    private Integer ejemplaresDisponibles;
}
