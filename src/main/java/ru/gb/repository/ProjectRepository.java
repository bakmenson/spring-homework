package ru.gb.repository;

import org.springframework.stereotype.Repository;
import ru.gb.model.Project;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class ProjectRepository {

    private static Long sequence = 1L;
    private final List<Project> projects = new ArrayList<>();

    public Optional<Project> getById(Long id) {
        return projects.stream()
                .filter(project -> Objects.equals(project.getId(), id))
                .findFirst();
    }

    public List<Project> getProjects() {
        return List.copyOf(projects);
    }

    public Project create(Project project) {
        project.setId(sequence++);
        projects.add(project);
        return project;
    }

    public Optional<Project> update(Long id, Project project) {
        Optional<Project> oldProject = projects.stream()
                .filter(it -> it.getId().equals(id))
                .findFirst();

        oldProject.ifPresent(name -> name.setName(project.getName()));

        return oldProject;
    }

    public void deleteById(Long id) {
        projects.stream()
                .filter(it -> Objects.equals(it.getId(), id))
                .findFirst()
                .ifPresent(projects::remove);
    }

}
