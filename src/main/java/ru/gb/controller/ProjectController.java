package ru.gb.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.gb.model.Project;
import ru.gb.model.Timesheet;
import ru.gb.service.ProjectService;

import java.util.List;
import java.util.Optional;

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
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Project> create(@RequestBody Project project) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(project));
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity<Project> update(@PathVariable Long id, @RequestBody Project project) {
        Optional<Project> update = service.update(id, project);

        return update.map(value -> ResponseEntity.ok(update.get()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/timesheets")
    public ResponseEntity<List<Timesheet>> getProjectTimesheets(@PathVariable Long id) {
        Optional<Project> project = service.findById(id);

        if (project.isPresent()) {
            return ResponseEntity.ok(service.getProjectTimesheets(id));
        }

        return ResponseEntity.notFound().build();
    }

}
