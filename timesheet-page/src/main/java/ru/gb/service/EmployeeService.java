package ru.gb.service;

import ru.gb.dto.EmployeePageDTO;
import ru.gb.dto.ProjectPageDTO;
import ru.gb.dto.TimesheetPageDTO;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface EmployeeService {
    List<EmployeePageDTO> findAll();
    Optional<EmployeePageDTO> findById(Long id);
//    EmployeePageDTO create(EmployeePageDTO employee);
//    void deleteById(Long id);
//    Optional<EmployeePageDTO> update(Long id, EmployeePageDTO employee);
    List<TimesheetPageDTO> findByEmployeeId(Long id);
    List<ProjectPageDTO> findEmployeeProjects(Long id);
}
