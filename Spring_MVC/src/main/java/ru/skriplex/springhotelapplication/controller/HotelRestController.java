package ru.skriplex.springhotelapplication.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skriplex.springhotelapplication.model.Response;
import ru.skriplex.springhotelapplication.exception.HotelNotFoundException;
import ru.skriplex.springhotelapplication.model.HotelModel;
import ru.skriplex.springhotelapplication.service.HotelService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v2/hotels")
@Tag(name = "RestController",description = "REST Api")
public class HotelRestController {

    private final HotelService service;

    @Operation(summary = "Получение списка отелей")
    @ApiResponse(responseCode = "200", description = "Возврат массива объектов")
    @GetMapping
    public ResponseEntity<?> getAllHotels() {
        List<HotelModel> list = service.getAll().stream()
                .toList();
        return ResponseEntity.ok().body(list);
    }

    @Operation(summary = "Получение отеля по id")
    @ApiResponse(responseCode = "200", description = "Возврат объекта по id")
    @ApiResponse(responseCode = "404", description = "Если объект не найден")
    @GetMapping("/{id}")
    public ResponseEntity<?> getHotelById(@PathVariable("id") Long id) {
        HotelModel byId = service.getById(id);
        return ResponseEntity.ok().body(byId);
    }

    @Operation(summary = "Создание нового отеля")
    @ApiResponse(responseCode = "201", description = "При создании")
    @PostMapping
    public ResponseEntity<?> createHotel(@RequestBody HotelModel model) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(model));
    }

    @Operation(summary = "Обновление данных отеля по id")
    @ApiResponse(responseCode = "200", description = "Возврат объекта")
    @ApiResponse(responseCode = "404", description = "Если объект не найден по id")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateHotel(@PathVariable("id") Long id, @RequestBody HotelModel model) {
        model.setId(id);
        return ResponseEntity.ok(service.update(model));
    }

    @Operation(summary = "Удаление отеля по id")
    @ApiResponse(responseCode = "204", description = "После удаления")
    @ApiResponse(responseCode = "404", description = "Если объект не найден по id")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteHotelById(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @ExceptionHandler(HotelNotFoundException.class)
    ResponseEntity<?> handleExceptionHotelNotFound(HotelNotFoundException ex) {
        log.error("HotelNotFoundException: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(ex.getMessage()));
    }
}
