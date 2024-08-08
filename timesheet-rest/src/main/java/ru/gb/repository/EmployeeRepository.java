package ru.gb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.gb.model.Employee;
import ru.gb.model.Project;

import java.util.Set;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    @Query("select p from Project p")
    Set<Project> findAllProjects(Employee employee);
}
