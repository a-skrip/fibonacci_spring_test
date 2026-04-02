package ru.skriplex.springhotelapplication.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.Instant;

@Data
public class HotelModel {


    @Schema(description = "Id отеля", required = false, example = "1")
    private Long id;

    @NotBlank(message = "Название обязательно")
    @Size(min = 2, max = 255)
    @Schema(description = "Название отеля", required = true, example = "Grand Hotel")
    private String name;

    @NotNull(message = "Количество звезд обязательно")
    @Min(1)
    @Max(5)
    @Schema(description = "Количество звезд", required = true, example = "1")
    private Integer stars;

    @Schema(description = "Описание отеля", required = false, example = "Роскошный отель в центре города")
    private String description;

    @Schema(description = "Создано", required = false)
    private Instant creationDate;

    @Schema(description = "Обновлено", required = false)
    private Instant updatedDate;
}
