package ru.gb.service;

import org.springframework.stereotype.Service;
import ru.gb.model.Project;
import ru.gb.model.Timesheet;
import ru.gb.repository.ProjectRepository;
import ru.gb.repository.TimesheetRepository;

import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service // то же самое, что и Component
public class TimesheetService {

    private final TimesheetRepository repository;
    private final ProjectRepository projectRepository;

    public TimesheetService(TimesheetRepository repository, ProjectRepository projectRepository) {
        this.repository = repository;
        this.projectRepository = projectRepository;
    }

    public Optional<Timesheet> getById(Long id) {
        return repository.getById(id);
    }

    public List<Timesheet> getAll() {
        return repository.getAll();
    }

    public Timesheet create(Timesheet timesheet) {
        Long projectId = timesheet.getProjectId();

        if (projectId == null) {
            throw new NullPointerException("There is no project id.");
        }

        Optional<Project> project = projectRepository.getById(timesheet.getProjectId());

        if (project.isPresent()) {
            timesheet.setCreatedAt(LocalDate.now());
            return repository.create(timesheet);
        }

        throw new NoSuchElementException("There is no project with id " + projectId + ".");
    }

    public void delete(Long id) {
        repository.delete(id);
    }

    public List<Timesheet> getCreatedAfter(LocalDate date) {
        return repository.getAll().stream()
                .filter(timesheet -> timesheet.getCreatedAt().isAfter(ChronoLocalDate.from(date)))
                .toList();
    }

    public List<Timesheet> getCreatedBefore(LocalDate date) {
        return repository.getAll().stream()
                .filter(timesheet -> timesheet.getCreatedAt().isBefore(ChronoLocalDate.from(date)))
                .toList();
    }
}
