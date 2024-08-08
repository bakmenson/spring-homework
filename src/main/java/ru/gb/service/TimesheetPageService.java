package ru.gb.service;

import ru.gb.controller.TimesheetPageDto;

import java.util.List;
import java.util.Optional;

public interface TimesheetPageService {
    List<TimesheetPageDto> findAll();
    Optional<TimesheetPageDto> findById(Long id);
}
