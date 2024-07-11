package ru.gb.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.gb.model.Timesheet;
import ru.gb.service.TimesheetService;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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

    public TimesheetController(TimesheetService service) {
        this.service = service;
    }

    // /timesheets/{id}
    @GetMapping("/{id}") // получить все
    public ResponseEntity<Timesheet> get(@PathVariable Long id) {
        Optional<Timesheet> ts = service.findById(id);

        //      return ResponseEntity.ok().body(ts.get());
        return ts.map(timesheet -> ResponseEntity.status(HttpStatus.OK).body(timesheet))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping // получить все
    public ResponseEntity<List<Timesheet>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);

        // 204 No Content
        return ResponseEntity.noContent().build();
    }

    @GetMapping(params = "createdAfter")
    public ResponseEntity<List<Timesheet>> getCreatedAfter(@RequestParam LocalDate createdAfter) {
        return ResponseEntity.ok(service.getCreatedAfter(createdAfter));
    }

    @GetMapping(params = "createdBefore")
    public ResponseEntity<List<Timesheet>> getCreatedBefore(@RequestParam LocalDate createdBefore) {
        return ResponseEntity.ok(service.getCreatedBefore(createdBefore));
    }

}
