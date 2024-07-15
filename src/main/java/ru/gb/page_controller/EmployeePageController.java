package ru.gb.page_controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.gb.model.Employee;
import ru.gb.model.Project;
import ru.gb.model.Timesheet;
import ru.gb.service.EmployeeService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Controller
@RequestMapping("/home/employees")
public class EmployeePageController {

    private final EmployeeService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public String getEmployees(Model model) {
        model.addAttribute("employees", service.findAll());
        return "employees-page";
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable Long id, Model model) {
        Optional<Employee> employee = service.findById(id);
        List<Timesheet> timesheets = service.findByEmployeeId(id);
        Set<Project> projects = service.findEmployeeProjects(id);

        if (employee.isPresent()) {
            model.addAttribute("employee", employee.get());
            model.addAttribute("timesheets", timesheets);
            model.addAttribute("projects", projects);
            return "employee-page";
        }

        throw new NoSuchElementException("There is no employee with id #" + id);
    }

}
