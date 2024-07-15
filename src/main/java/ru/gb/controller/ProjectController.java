package ru.gb.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.gb.exception.ResourceNotFoundException;
import ru.gb.model.Employee;
import ru.gb.model.Project;
import ru.gb.model.Timesheet;
import ru.gb.service.ProjectService;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService service;

    @GetMapping
    public ResponseEntity<List<Project>> getProjects() {
        return ResponseEntity.ok(service.getProjects());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> getById(@PathVariable Long id) {
        Optional<Project> project = service.findById(id);

        return project
                .map(value -> ResponseEntity.ok(project.get()))
                .orElseThrow(() -> new ResourceNotFoundException("There is no project with id #" + id));
    }

    @PostMapping
    public ResponseEntity<Project> create(@RequestBody Project project) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(project));
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity<Project> update(@PathVariable Long id, @RequestBody Project project) {
        Optional<Project> update = service.update(id, project);

        return update.map(value -> ResponseEntity.ok(update.get()))
                .orElseThrow(() -> new ResourceNotFoundException("There is no employee with id #" + id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (service.findById(id).isPresent()) {
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        }

        throw new ResourceNotFoundException("There is no project with id #" + id);
    }

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
