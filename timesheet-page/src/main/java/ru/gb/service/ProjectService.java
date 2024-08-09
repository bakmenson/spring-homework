package ru.gb.service;

import jakarta.validation.constraints.NotNull;
import ru.gb.dto.EmployeePageDTO;
import ru.gb.dto.ProjectPageDTO;
import ru.gb.dto.TimesheetPageDTO;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ProjectService {
    List<ProjectPageDTO> findAll();
    Optional<ProjectPageDTO> findById(@NotNull Long id);
//    Project create(@NotNull Project project);
//    void deleteById(@NotNull Long id);
    List<TimesheetPageDTO> findProjectTimesheets(@NotNull Long id);
//    Optional<Project> update(@NotNull Long id, @NotNull Project project);
    Set<EmployeePageDTO> findProjectEmployees(@NotNull Long id);
}
