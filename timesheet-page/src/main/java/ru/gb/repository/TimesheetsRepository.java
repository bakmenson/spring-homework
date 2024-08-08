package ru.gb.repository;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.client.RestClient;
import ru.gb.client.TimesheetResponse;
import ru.gb.dto.TimesheetPageDTO;

import java.util.List;
import java.util.Optional;

public interface TimesheetsRepository {

    Optional<TimesheetPageDTO> getTimesheetPageDTO(@NotNull TimesheetResponse timesheetResponse, @NotNull RestClient restClient);
    Optional<TimesheetPageDTO> getTimesheetPageDTO(@NotNull Long timesheetId, @NotNull RestClient restClient);
    List<TimesheetPageDTO> findTimesheets(@NotNull String uri, @NotNull Long id, @NotNull RestClient restClient);
}
