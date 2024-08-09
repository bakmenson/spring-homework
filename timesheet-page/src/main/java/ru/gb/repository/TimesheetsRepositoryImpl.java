package ru.gb.repository;

import jakarta.validation.constraints.NotNull;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import ru.gb.client.EmployeeResponse;
import ru.gb.client.ProjectResponse;
import ru.gb.client.TimesheetResponse;
import ru.gb.dto.TimesheetPageDTO;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class TimesheetsRepositoryImpl implements TimesheetsRepository {

    @Override
    public Optional<TimesheetPageDTO> getTimesheetPageDTO(TimesheetResponse timesheetResponse, RestClient restClient) {
        if (timesheetResponse != null) {
            TimesheetPageDTO timesheetPageDTO = new TimesheetPageDTO();
            timesheetPageDTO.setId(String.valueOf(timesheetResponse.getId()));
            timesheetPageDTO.setMinutes(String.valueOf(timesheetResponse.getMinutes()));
            timesheetPageDTO.setCreatedAt(timesheetResponse.getCreatedAt().format(DateTimeFormatter.ISO_DATE));
            timesheetPageDTO.setProjectId(String.valueOf(timesheetResponse.getProjectId()));
            timesheetPageDTO.setEmployeeId(String.valueOf(timesheetResponse.getEmployeeId()));

            Optional<ProjectResponse> projectResponse = getProjectResponse(timesheetResponse.getProjectId(), restClient);
            projectResponse.ifPresent(value -> timesheetPageDTO.setProjectName(value.getName()));

            Optional<EmployeeResponse> employeeResponse = getEmployeeResponse(timesheetResponse.getEmployeeId(), restClient);
            if (employeeResponse.isPresent()) {
                timesheetPageDTO.setEmployeeName(employeeResponse.get().getName());
                timesheetPageDTO.setEmployeeSurname(employeeResponse.get().getSurname());
            }

            return Optional.of(timesheetPageDTO);
        }

        return Optional.empty();
    }

    @Override
    public Optional<TimesheetPageDTO> getTimesheetPageDTO(Long timesheetId, RestClient restClient) {
        try {
            TimesheetResponse response = restClient.get()
                    .uri("/timesheets/" + timesheetId)
                    .retrieve()
                    .body(TimesheetResponse.class);

            TimesheetPageDTO timesheetPageDTO = new TimesheetPageDTO();
            timesheetPageDTO.setId(String.valueOf(response.getId()));
            timesheetPageDTO.setMinutes(String.valueOf(response.getMinutes()));
            timesheetPageDTO.setCreatedAt(response.getCreatedAt().format(DateTimeFormatter.ISO_DATE));
            timesheetPageDTO.setProjectId(String.valueOf(response.getProjectId()));
            timesheetPageDTO.setEmployeeId(String.valueOf(response.getEmployeeId()));

            Optional<ProjectResponse> projectResponse = getProjectResponse(response.getProjectId(), restClient);
            projectResponse.ifPresent(value -> timesheetPageDTO.setProjectName(value.getName()));

            Optional<EmployeeResponse> employeeResponse = getEmployeeResponse(response.getEmployeeId(), restClient);

            if (employeeResponse.isPresent()) {
                timesheetPageDTO.setEmployeeName(employeeResponse.get().getName());
                timesheetPageDTO.setEmployeeSurname(employeeResponse.get().getSurname());
            }

            return Optional.of(timesheetPageDTO);
        } catch (HttpClientErrorException.NotFound e) {
            return Optional.empty();
        }
    }

    @Override
    public List<TimesheetPageDTO> findTimesheets(String uri, Long id, @NotNull RestClient restClient) {
        List<TimesheetPageDTO> result = new ArrayList<>();

        Optional<List<TimesheetResponse>> timesheets;

        try {
            timesheets = Optional.ofNullable(restClient.get()
                    .uri(uri + id + "/timesheets")
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<TimesheetResponse>>() {
                    }));

        } catch (HttpClientErrorException.NotFound e) {
            return result;
        }

        if (timesheets.isPresent()) {
            for (TimesheetResponse timesheetResponse : timesheets.get()) {
                Optional<TimesheetPageDTO> timesheetPageDTO = getTimesheetPageDTO(timesheetResponse, restClient);
                timesheetPageDTO.ifPresent(result::add);
            }
        }

        return result;
    }

    private Optional<ProjectResponse> getProjectResponse(@NotNull Long projectId, RestClient restClient) {
        try {
            return Optional.ofNullable(restClient.get()
                    .uri("/projects/" + projectId)
                    .retrieve()
                    .body(ProjectResponse.class));
        } catch (HttpClientErrorException.NotFound e) {
            return Optional.empty();
        }
    }

    private Optional<EmployeeResponse> getEmployeeResponse(@NotNull Long employeeId, RestClient restClient) {
        try {
            return Optional.ofNullable(restClient.get()
                    .uri("/employees/" + employeeId)
                    .retrieve()
                    .body(EmployeeResponse.class));
        } catch (HttpClientErrorException.NotFound e) {
            return Optional.empty();
        }
    }

}
