package ru.gb.service;

import ru.gb.model.Employee;
import ru.gb.model.Timesheet;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    List<Employee> findAll();
    Optional<Employee> findById(Long id);
    Employee create(Employee employee);
    void deleteById(Long id);
    Optional<Employee> update(Long id, Employee employee);
    List<Timesheet> findByEmployeeId(Long id);
}
