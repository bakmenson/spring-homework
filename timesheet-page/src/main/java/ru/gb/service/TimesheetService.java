package ru.gb.service;

import ru.gb.dto.TimesheetPageDTO;

import java.util.List;
import java.util.Optional;

public interface TimesheetService {

    List<TimesheetPageDTO> findAll();
    Optional<TimesheetPageDTO> findById(Long id);

}
