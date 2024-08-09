package ru.gb.service;

import ru.gb.model.Timesheet;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TimesheetService {
    Optional<Timesheet> findById(Long id);
    List<Timesheet> findAll();
    Timesheet create(Timesheet timesheet);
    void delete(Long id);
    List<Timesheet> getCreatedAfter(LocalDate date);
    List<Timesheet> getCreatedBefore(LocalDate date);
}
