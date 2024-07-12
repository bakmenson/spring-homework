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
public class TimesheetServiceImpl implements TimesheetService {

    private final TimesheetRepository repository;
    private final ProjectRepository projectRepository;

    public TimesheetServiceImpl(TimesheetRepository repository, ProjectRepository projectRepository) {
        this.repository = repository;
        this.projectRepository = projectRepository;
    }

    @Override
    public Optional<Timesheet> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Timesheet> findAll() {
        return repository.findAll();
    }

    @Override
    public Timesheet create(Timesheet timesheet) {
        Long projectId = timesheet.getProjectId();

        if (projectId == null) {
            throw new NullPointerException("There is no project id.");
        }

        Optional<Project> project = projectRepository.findById(timesheet.getProjectId());

        if (project.isPresent()) {
            timesheet.setCreatedAt(LocalDate.now());
            return repository.save(timesheet);
        }

        throw new NoSuchElementException("There is no project with id " + projectId + ".");
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<Timesheet> getCreatedAfter(LocalDate date) {
        return repository.findAll().stream()
                .filter(timesheet -> timesheet.getCreatedAt().isAfter(ChronoLocalDate.from(date)))
                .toList();
    }

    @Override
    public List<Timesheet> getCreatedBefore(LocalDate date) {
        return repository.findAll().stream()
                .filter(timesheet -> timesheet.getCreatedAt().isBefore(ChronoLocalDate.from(date)))
                .toList();
    }
}
