package ru.gb.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gb.model.Employee;
import ru.gb.model.Project;
import ru.gb.model.Timesheet;
import ru.gb.repository.ProjectRepository;
import ru.gb.repository.TimesheetRepository;

import java.util.*;

@RequiredArgsConstructor
@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository repository;
    private final TimesheetRepository timesheetRepository;

    @Override
    public List<Project> getProjects() {
        return repository.findAll();
    }

    @Override
    public Optional<Project> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Project create(Project project) {
        return repository.save(project);
    }

    @Override
    public Optional<Project> update(Long id, Project project) {
        Optional<Project> needUpdate = findById(id);

        if (needUpdate.isPresent()) {
            project.setId(id);
            repository.save(project);
            return Optional.of(project);
        }

        return Optional.empty();
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<Timesheet> getProjectTimesheets(Long id) {
        if (findById(id).isPresent()) {
            return timesheetRepository.findByProjectId(id);
        }
        return Collections.emptyList();
    }

    @Override
    public Set<Employee> findProjectEmployees(Long id) {
        Optional<Project> project = findById(id);

        if (project.isPresent()) {
            return repository.findAllEmployee(project.get());
        }

        return Collections.emptySet();
    }

}
