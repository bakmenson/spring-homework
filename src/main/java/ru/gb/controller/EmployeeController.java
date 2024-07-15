package ru.gb.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.gb.model.Employee;
import ru.gb.model.Project;
import ru.gb.model.Timesheet;
import ru.gb.service.EmployeeServiceImpl;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Employee> create(@RequestBody Employee employee) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(employee));
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity<Employee> update(@PathVariable Long id, @RequestBody Employee employee) {
        Optional<Employee> update = service.update(id, employee);

        return update.map(value -> ResponseEntity.ok(update.get()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/timesheets")
    public ResponseEntity<List<Timesheet>> getEmployeeTimesheets(@PathVariable Long id) {
        Optional<Employee> employee = service.findById(id);

        if (employee.isPresent()) {
            return ResponseEntity.ok(service.findByEmployeeId(id));
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/projects")
    public ResponseEntity<Set<Project>> getEmployeeProjects(@PathVariable Long id) {
        Set<Project> projects = service.findEmployeeProjects(id);

        if (projects.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(projects);
    }

}
