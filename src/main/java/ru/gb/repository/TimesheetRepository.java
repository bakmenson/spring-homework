package ru.gb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.gb.model.Timesheet;

import java.util.Optional;

@Repository // @Component для классов, работающих с данными
public interface TimesheetRepository extends JpaRepository<Timesheet, Long> {
}
