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
import ru.gb.model.Employee;
import ru.gb.model.Project;
import ru.gb.model.Timesheet;
import ru.gb.service.ProjectService;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Secured("rest")
@Tag(name = "Projects", description = "API для работы с проектами")
@RequiredArgsConstructor
@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService service;

    @Operation(
            summary = "Получить все проекты",
            description = "Метод возвращает все проекты в виде списка"
    )
    @ControllerApiResponse.OkResponse
    @ControllerApiResponse.ServerErrorResponse
    @GetMapping
    public ResponseEntity<List<Project>> getProjects() {
        return ResponseEntity.ok(service.getProjects());
    }

    @Operation(
            summary = "Получить проект по идентификатору",
            description = "Метод находит и возвращает прокет по идентификатору. Если такого проекта нет, то выбрасывает ошибку ResourceNotFoundException",
            parameters = {
                    @Parameter(name = "id", description = "Необходим для поиска проекта по индентификаторку", required = true)
            }
    )
    @ControllerApiResponse.OkResponse
    @ControllerApiResponse.NotFoundResponse
    @ControllerApiResponse.ServerErrorResponse
    @GetMapping("/{id}")
    public ResponseEntity<Project> getById(@PathVariable Long id) {
        Optional<Project> project = service.findById(id);

        return project
                .map(value -> ResponseEntity.ok(project.get()))
                .orElseThrow(() -> new ResourceNotFoundException("There is no project with id #" + id));
    }

    @Operation(
            summary = "Создать проект",
            description = "Метод создает создает проект и сохранить в базе",
            parameters = {
                    @Parameter(name = "employee", description = "Параметр принимает объект Project для дальнейшего сохранения в базе")
            }
    )
    @ControllerApiResponse.OkResponse
    @ControllerApiResponse.ServerErrorResponse
    @PostMapping
    public ResponseEntity<Project> create(@RequestBody Project project) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(project));
    }

    @Operation(
            summary = "Обновить проект",
            description = "Метод обновляет проект существующего проекта по идентификатору. Если такого сотрудника нет, то выбрасывает ошибку ResourceNotFoundException",
            parameters = {
                    @Parameter(name = "id", description = "Необходим для поиска проекта по индентификаторку"),
                    @Parameter(name = "employee", description = "Параметр принимает объект Project для дальнейшего обновления в базе записи по id")
            }
    )
    @ControllerApiResponse.OkResponse
    @ControllerApiResponse.NotFoundResponse
    @ControllerApiResponse.ServerErrorResponse
    @PutMapping(path = "/{id}")
    public ResponseEntity<Project> update(@PathVariable Long id, @RequestBody Project project) {
        Optional<Project> update = service.update(id, project);

        return update.map(value -> ResponseEntity.ok(update.get()))
                .orElseThrow(() -> new ResourceNotFoundException("There is no employee with id #" + id));
    }

    @Operation(
            summary = "Удалить проект по идентификатору",
            description = "Метод удаляет проект по идентификатору. Если такого проекта нет, то выбрасывает ошибку ResourceNotFoundException",
            parameters = {
                    @Parameter(name = "id", description = "Необходим для поиска проекта по индентификаторку")
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

        throw new ResourceNotFoundException("There is no project with id #" + id);
    }

    @Operation(
            summary = "Получаем все записи учета рабочего времени в проекте",
            description = "Метод возвращает все записи учета рабочего времени в проекте по идентификатору проекта в виде списка. Если такого проекта нет, то выбрасывает ошибку ResourceNotFoundException",
            parameters = {
                    @Parameter(name = "id", description = "Необходим для поиска проекта по индентификаторку")
            }
    )
    @ControllerApiResponse.OkResponse
    @ControllerApiResponse.NotFoundResponse
    @ControllerApiResponse.ServerErrorResponse
    @GetMapping("/{id}/timesheets")
    public ResponseEntity<List<Timesheet>> getProjectTimesheets(@PathVariable Long id) {
        if (service.findById(id).isPresent()) {
            List<Timesheet> timesheets = service.getProjectTimesheets(id);

            if (timesheets.isEmpty()) {
                throw new ResourceNotFoundException("There is no timesheets with project id #" + id);
            }

            return ResponseEntity.ok(timesheets);
        }

        throw new ResourceNotFoundException("There is no project with id #" + id);
    }

    @Operation(
            summary = "Получаем всех сотрудников на проекте",
            description = "Метод возвращает всех сотрудников на проекте по идентификатору проекта в виде списка. Если такого проекта нет, то выбрасывает ошибку ResourceNotFoundException",
            parameters = {
                    @Parameter(name = "id", description = "Необходим для поиска проекта по индентификаторку")
            }
    )
    @ControllerApiResponse.OkResponse
    @ControllerApiResponse.NotFoundResponse
    @ControllerApiResponse.ServerErrorResponse
    @GetMapping("/{id}/employees")
    public ResponseEntity<Set<Employee>> getProjectEmployees(@PathVariable Long id) {
        if (service.findById(id).isPresent()) {
            Set<Employee> employees =  service.findProjectEmployees(id);

            if (employees.isEmpty()) {
                throw new ResourceNotFoundException("There is no employees with project id #" + id);
            }

            return ResponseEntity.ok(employees);
        }

        throw new ResourceNotFoundException("There is no project with id #" + id);
    }

}
