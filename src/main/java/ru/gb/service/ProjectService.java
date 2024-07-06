package ru.gb.service;

import org.springframework.stereotype.Service;
import ru.gb.model.Project;
import ru.gb.repository.ProjectRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    private final ProjectRepository repository;

    public ProjectService(ProjectRepository repository) {
        this.repository = repository;
    }

    public List<Project> getProjects() {
        return repository.getProjects();
    }

    public Optional<Project> getById(Long id) {
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

}
