package ru.gb.service;

import ru.gb.model.Employee;
import ru.gb.model.Project;
import ru.gb.model.Timesheet;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ProjectService {
    List<Project> getProjects();
    Optional<Project> findById(Long id);
    Project create(Project project);
    void deleteById(Long id);
    List<Timesheet> getProjectTimesheets(Long id);
    Optional<Project> update(Long id, Project project);
    Set<Employee> findProjectEmployees(Long id);
}
