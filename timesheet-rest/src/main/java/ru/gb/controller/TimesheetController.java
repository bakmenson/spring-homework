package ru.gb.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import ru.gb.exception.ResourceNotFoundException;
import ru.gb.model.Timesheet;
import ru.gb.service.TimesheetService;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Secured("rest")
@Tag(name = "Timesheets", description = "API для работы с записями учета рабочего времени")
@RequiredArgsConstructor
@RestController
@RequestMapping("/timesheets")
public class TimesheetController {

    // GET - получить - не содержит тела
    // POST - create
    // PUT - изменение
    // PATCH - изменение
    // DELETE - удаление

    // @GetMapping("/timesheets/{id}") // получить конкретную запись по идентификатору
    // @DeleteMapping("/timesheets/{id}") // удалить конкретную запись по идентификатору
    // @PutMapping("/timesheets/{id}") // обновить конкретную запись по идентификатору

    private final TimesheetService service;

    @Operation(
            summary = "Получить запись учета рабочего времени по идентификатору",
            description = "Метод находит и возвращает запись учета рабочего времени по идентификатору по идентификатору. Если такого записи нет, то выбрасывает ошибку ResourceNotFoundException",
            parameters = {
                    @Parameter(name = "id", description = "Необходим для поиска записи учета рабочего времени по индентификаторку", required = true)
            }
    )
    @ControllerApiResponse.OkResponse
    @ControllerApiResponse.NotFoundResponse
    @ControllerApiResponse.ServerErrorResponse
    @GetMapping("/{id}") // получить все
    public ResponseEntity<Timesheet> get(@PathVariable Long id) {
        Optional<Timesheet> ts = service.findById(id);

        return ts.map(timesheet -> ResponseEntity.status(HttpStatus.OK).body(timesheet))
                .orElseThrow(() -> new ResourceNotFoundException("There is no timesheet with id #" + id));
    }

    @Operation(
            summary = "Получить все записи учета рабочего времени",
            description = "Метод возвращает все записи учета рабочего времени"
    )
    @ControllerApiResponse.OkResponse
    @ControllerApiResponse.NotFoundResponse
    @ControllerApiResponse.ServerErrorResponse
    @GetMapping // получить все
    public ResponseEntity<List<Timesheet>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(
            summary = "Создать запись учета рабочего времени",
            description = "Метод создает запись учета рабочего времени и сохраняет в базе. Медот может выбрасить NullPointerException или NoSuchElementException.",
            responses = {
                    @ApiResponse(
                            description = "Не возможно сохранить запись учета рабочего времени",
                            responseCode = "400"
                    )
            },
            parameters = {
                    @Parameter(name = "timesheet", description = "Параметр принимает объект Timesheet для дальнейшего сохранения в базе")
            }
    )
    @ControllerApiResponse.OkResponse
    @ControllerApiResponse.ServerErrorResponse
    @PostMapping // создание нового ресурса
    public ResponseEntity<Timesheet> create(@RequestBody Timesheet timesheet) {
        try {
            timesheet = service.create(timesheet);
        } catch (NullPointerException | NoSuchElementException e) {
            System.err.println("Error while creating timesheet. " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }

        // 201 Created
        return ResponseEntity.status(HttpStatus.CREATED).body(timesheet);
    }

    @Operation(
            summary = "Удалить удаляет запись учета рабочего времени по идентификатору",
            description = "Метод удаляет запись учета рабочего времени по идентификатору. Если такой записи нет, то выбрасывает ошибку ResourceNotFoundException",
            parameters = {
                    @Parameter(name = "id", description = "Необходим для поиска записи учета рабочего времени по индентификаторку")
            }
    )
    @ControllerApiResponse.NoContentResponse
    @ControllerApiResponse.NotFoundResponse
    @ControllerApiResponse.ServerErrorResponse
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (service.findById(id).isPresent()) {
            service.delete(id);
            // 204 No Content
            return ResponseEntity.noContent().build();
        }

        throw new ResourceNotFoundException("There is no timesheet with id #" + id);
    }

    @Operation(
            summary = "Получить запись учета рабочего времени",
            description = "Метод возвращает запись учета рабочего времени, созданной после указанной даты",
            parameters = {
                    @Parameter(name = "createdAfter", description = "Необходим для фильтрации записи учета рабочего времени по дате создания после даты")
            }
    )
    @ControllerApiResponse.ServerErrorResponse
    @ControllerApiResponse.OkResponse
    @GetMapping(params = "createdAfter")
    public ResponseEntity<List<Timesheet>> getCreatedAfter(@RequestParam LocalDate createdAfter) {
        return ResponseEntity.ok(service.getCreatedAfter(createdAfter));
    }

    @Operation(
            summary = "Получить запись учета рабочего времени",
            description = "Метод возвращает запись учета рабочего времени, созданной до указанной даты",
            parameters = {
                    @Parameter(name = "createdBefore", description = "Необходим для фильтрации записи учета рабочего времени по дате создания до даты")
            }
    )
    @ControllerApiResponse.ServerErrorResponse
    @ControllerApiResponse.OkResponse
    @GetMapping(params = "createdBefore")
    public ResponseEntity<List<Timesheet>> getCreatedBefore(@RequestParam LocalDate createdBefore) {
        return ResponseEntity.ok(service.getCreatedBefore(createdBefore));
    }

}
