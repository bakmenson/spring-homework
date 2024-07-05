package ru.gb.service;

import org.springframework.stereotype.Service;
import ru.gb.model.Timesheet;
import ru.gb.repository.TimesheetRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service // то же самое, что и Component
public class TimesheetService {

  private final TimesheetRepository repository;

  public TimesheetService(TimesheetRepository repository) {
    this.repository = repository;
  }

  public Optional<Timesheet> getById(Long id) {
    return repository.getById(id);
  }

  public List<Timesheet> getAll() {
    return repository.getAll();
  }

  public Timesheet create(Timesheet timesheet) {
    timesheet.setCreatedAt(LocalDate.now());
    return repository.create(timesheet);
  }

  public void delete(Long id) {
    repository.delete(id);
  }

}
