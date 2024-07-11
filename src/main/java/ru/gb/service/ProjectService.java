package ru.gb.service;

import org.springframework.stereotype.Service;
import ru.gb.model.Project;
import ru.gb.model.Timesheet;
import ru.gb.repository.ProjectRepository;
import ru.gb.repository.TimesheetRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    private final ProjectRepository repository;
    private final TimesheetRepository timesheetRepository;

    public ProjectService(ProjectRepository repository, TimesheetRepository timesheetRepository) {
        this.repository = repository;
        this.timesheetRepository = timesheetRepository;
    }

    public List<Project> getProjects() {
        return repository.getProjects();
    }

    public Optional<Project> findById(Long id) {
        return repository.getById(id);
    }

    public Project create(Project project) {
        return repository.create(project);
    }

    public Optional<Project> update(Long id, Project project) {
        return repository.update(id, project);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public List<Timesheet> getProjectTimesheets(Long id) {
        return timesheetRepository.getAll().stream()
                .filter(timesheet -> timesheet.getProjectId().equals(id))
                .toList();
    }

}
