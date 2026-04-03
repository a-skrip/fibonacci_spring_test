package ru.skriplex.springhotelapplication.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.Instant;

@Data
public class HotelModel {


    @Schema(description = "Id отеля", example = "1")
    private Long id;

    @NotBlank()
    @Size(min = 2, max = 255)
    @Schema(description = "Название отеля", required = true, example = "Grand Hotel")
    private String name;

    @NotNull()
    @Min(1)
    @Max(5)
    @Schema(description = "Количество звезд", required = true, example = "4")
    private Integer stars;

    @Schema(description = "Описание отеля", example = "Роскошный отель в центре города")
    private String description;

    @Schema(description = "Создано", example = "2026-04-02 14:03:08.915449 +00:00")
    private Instant creationDate;

    @Schema(description = "Обновлено", example = "2026-04-02 14:03:08.915449 +00:00")
    private Instant updatedDate;
}
