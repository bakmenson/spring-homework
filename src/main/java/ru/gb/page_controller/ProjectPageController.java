package ru.gb.page_controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.gb.model.Project;
import ru.gb.service.ProjectService;

import java.util.NoSuchElementException;
import java.util.Optional;

@Controller
@RequestMapping("/home/projects")
public class ProjectPageController {

    private final ProjectService service;

    public ProjectPageController(ProjectService service) {
        this.service = service;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public String getProjects(Model model) {
        model.addAttribute("projects", service.getProjects());
        return "projects-page";
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable Long id, Model model) {
        Optional<Project> project = service.findById(id);

        if (project.isPresent()) {
            model.addAttribute(project.get());
            return "project-page";
        }

        throw new NoSuchElementException("There is no project with id #" + id);
    }

}
