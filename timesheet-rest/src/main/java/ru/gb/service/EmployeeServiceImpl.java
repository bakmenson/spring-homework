package ru.gb.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gb.model.Employee;
import ru.gb.model.Project;
import ru.gb.model.Timesheet;
import ru.gb.repository.EmployeeRepository;
import ru.gb.repository.TimesheetRepository;

import java.util.*;

@RequiredArgsConstructor
@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository repository;
    private final TimesheetRepository timesheetRepository;

    @Override
    public List<Employee> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Employee> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Employee create(Employee employee) {
        return repository.save(employee);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Optional<Employee> update(Long id, Employee employee) {
        Optional<Employee> needUpdate = findById(id);

        if (needUpdate.isPresent()) {
            employee.setId(id);
            repository.save(employee);
            return Optional.of(employee);
        }

        return Optional.empty();
    }

    @Override
    public List<Timesheet> findByEmployeeId(Long id) {
        return timesheetRepository.findByEmployeeId(id);
    }

    @Override
    public Set<Project> findEmployeeProjects(Long id) {
        Optional<Employee> employee = findById(id);

        if (employee.isPresent()) {
            return repository.findAllProjects(employee.get());
        }

        return Collections.emptySet();
    }

}
