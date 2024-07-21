package ru.gb.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.gb.exception.ResourceNotFoundException;
import ru.gb.model.Employee;
import ru.gb.model.Project;
import ru.gb.model.Timesheet;
import ru.gb.service.EmployeeServiceImpl;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Tag(name = "Employees", description = "API для работы с сотрудниками")
@RequiredArgsConstructor
@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeServiceImpl service;

    @Operation(
            summary = "Получить всех сотрудников",
            description = "Метод возвращает всех сотрудников в виде списка"
    )
    @ControllerApiResponse.OkResponse
    @ControllerApiResponse.ServerErrorResponse
    @GetMapping
    public ResponseEntity<List<Employee>> getEmployees() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(
            summary = "Получить сотрудника по идентификатору",
            description = "Метод находит и возвращает сотрудника по идентификатору. Если такого сотрудника нет, то выбрасывает ошибку ResourceNotFoundException",
            parameters = {
                    @Parameter(name = "id", description = "Необходим для поиска сотрудника по индентификаторку", required = true)
            }

    )
    @ControllerApiResponse.OkResponse
    @ControllerApiResponse.NotFoundResponse
    @ControllerApiResponse.ServerErrorResponse
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getById(@PathVariable Long id) {
        Optional<Employee> employee = service.findById(id);

        return employee
                .map(value -> ResponseEntity.ok(employee.get()))
                .orElseThrow(() -> new ResourceNotFoundException("There is no employee with id #" + id));
    }

    @Operation(
            summary = "Создать сотрудника и сохранить",
            description = "Метод создает сотрудника и сохранить в базе",
            parameters = {
                    @Parameter(name = "employee", description = "Параметр принимает объект Employee для дальнейшего сохранения в базе")
            }
    )
    @ControllerApiResponse.OkResponse
    @ControllerApiResponse.ServerErrorResponse
    @PostMapping
    public ResponseEntity<Employee> create(@RequestBody Employee employee) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(employee));
    }

    @Operation(
            summary = "Обновить сотрудника",
            description = "Метод обновляет данные существующего сотрудника по идентификатору. Если такого сотрудника нет, то выбрасывает ошибку ResourceNotFoundException",
            parameters = {
                    @Parameter(name = "id", description = "Необходим для поиска сотрудника по индентификаторку"),
                    @Parameter(name = "employee", description = "Параметр принимает объект Employee для дальнейшего обновления в базе записи по id")
            }
    )
    @ControllerApiResponse.OkResponse
    @ControllerApiResponse.NotFoundResponse
    @ControllerApiResponse.ServerErrorResponse
    @PutMapping(path = "/{id}")
    public ResponseEntity<Employee> update(@PathVariable Long id, @RequestBody Employee employee) {
        Optional<Employee> update = service.update(id, employee);

        return update.map(value -> ResponseEntity.ok(update.get()))
                .orElseThrow(() -> new ResourceNotFoundException("There is no employee with id #" + id));
    }

    @Operation(
            summary = "Удалить сотрудника",
            description = "Метод удаляет сотрудника по идентификатору. Если такого сотрудника нет, то выбрасывает ошибку ResourceNotFoundException",
            parameters = {
                    @Parameter(name = "id", description = "Необходим для поиска сотрудника по индентификаторку")
            }
    )
    @ControllerApiResponse.NoContentResponse
    @ControllerApiResponse.NotFoundResponse
    @ControllerApiResponse.ServerErrorResponse
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (service.findById(id).isPresent()) {
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        }

        throw new ResourceNotFoundException("There is no employee with id #" + id);
    }

    @Operation(
            summary = "Получить все записи учета рабочего времени сотрудника",
            description = "Метод возвращеет все записи учета рабочего времени сотрудника. Если такого сотрудника нет, то выбрасывает ошибку ResourceNotFoundException",
            parameters = {
                    @Parameter(name = "id", description = "Необходим для поиска сотрудника по индентификаторку")
            }
    )
    @ControllerApiResponse.OkResponse
    @ControllerApiResponse.NotFoundResponse
    @ControllerApiResponse.ServerErrorResponse
    @GetMapping("/{id}/timesheets")
    public ResponseEntity<List<Timesheet>> getEmployeeTimesheets(@PathVariable Long id) {
        if (service.findById(id).isPresent()) {
            return ResponseEntity.ok(service.findByEmployeeId(id));
        }

        throw new ResourceNotFoundException("There is no employee with id #" + id);
    }

    @Operation(
            summary = "Получить все проекты сотрудника",
            description = "Метод возвращеет все проекты сотрудника. Если такого сотрудника нет, то выбрасывает ошибку ResourceNotFoundException",
            parameters = {
                    @Parameter(name = "id", description = "Необходим для поиска сотрудника по индентификаторку")
            }
    )
    @ControllerApiResponse.OkResponse
    @ControllerApiResponse.NotFoundResponse
    @ControllerApiResponse.ServerErrorResponse
    @GetMapping("/{id}/projects")
    public ResponseEntity<Set<Project>> getEmployeeProjects(@PathVariable Long id) {
        if (service.findById(id).isPresent()) {
            Set<Project> projects = service.findEmployeeProjects(id);

            if (projects.isEmpty()) {
                throw new ResourceNotFoundException("There is no projects with employee id #" + id);
            }

            return ResponseEntity.ok(projects);
        }

        throw new ResourceNotFoundException("There is no employee with id #" + id);
    }

}
