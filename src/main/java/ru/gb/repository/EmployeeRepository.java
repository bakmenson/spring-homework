package ru.gb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.gb.model.Employee;
import ru.gb.model.Timesheet;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
