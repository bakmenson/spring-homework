package ru.gb.service;

import org.springframework.stereotype.Service;
import ru.gb.model.Project;
import ru.gb.model.Timesheet;
import ru.gb.repository.ProjectRepository;
import ru.gb.repository.TimesheetRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository repository;
    private final TimesheetRepository timesheetRepository;

    public ProjectServiceImpl(ProjectRepository repository, TimesheetRepository timesheetRepository) {
        this.repository = repository;
        this.timesheetRepository = timesheetRepository;
    }

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
        System.out.println(project);
        return repository.save(project);
    }

    @Override
    public Optional<Project> update(Long id, Project project) {
        return repository.update(id, project);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<Timesheet> getProjectTimesheets(Long id) {
        return timesheetRepository.findAll().stream()
                .filter(timesheet -> timesheet.getProjectId().equals(id))
                .toList();
    }

}
