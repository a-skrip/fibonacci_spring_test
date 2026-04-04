package ru.skriplex.springhotelapplication.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Response {
    @Schema(description = "ответ при ошибке", example = "Отель с id: 11 не найден")
    private String message;
}
