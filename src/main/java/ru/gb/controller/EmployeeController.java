package ru.gb.controller;

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
import java.util.function.Consumer;

@RequiredArgsConstructor
@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeServiceImpl service;

    @GetMapping
    public ResponseEntity<List<Employee>> getEmployees() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getById(@PathVariable Long id) {
        Optional<Employee> employee = service.findById(id);

        return employee
                .map(value -> ResponseEntity.ok(employee.get()))
                .orElseThrow(() -> new ResourceNotFoundException("There is no employee with id #" + id));
    }

    @PostMapping
    public ResponseEntity<Employee> create(@RequestBody Employee employee) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(employee));
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity<Employee> update(@PathVariable Long id, @RequestBody Employee employee) {
        Optional<Employee> update = service.update(id, employee);

        return update.map(value -> ResponseEntity.ok(update.get()))
                .orElseThrow(() -> new ResourceNotFoundException("There is no employee with id #" + id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (service.findById(id).isPresent()) {
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        }

        throw new ResourceNotFoundException("There is no employee with id #" + id);
    }

    @GetMapping("/{id}/timesheets")
    public ResponseEntity<List<Timesheet>> getEmployeeTimesheets(@PathVariable Long id) {
        if (service.findById(id).isPresent()) {
            return ResponseEntity.ok(service.findByEmployeeId(id));
        }

        throw new ResourceNotFoundException("There is no employee with id #" + id);
    }

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
