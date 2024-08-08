package ru.gb.repository;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import ru.gb.client.EmployeeResponse;
import ru.gb.dto.EmployeePageDTO;

import java.util.*;

@Component
public class EmployeeRepositoryImpl implements EmployeeRepository {

    @Override
    public Set<EmployeePageDTO> findEmployees(String uri, Long id, RestClient restClient) {
        Set<EmployeePageDTO> result = new HashSet<>();

        Optional<List<EmployeeResponse>> employees;

        try {
            employees = Optional.ofNullable(restClient.get()
                    .uri(uri + id + "/employees")
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<EmployeeResponse>>() {
                    }));

        } catch (HttpClientErrorException.NotFound e) {
            return result;
        }

        if (employees.isPresent()) {
            for (EmployeeResponse employeeResponse : employees.get()) {
                Optional<EmployeePageDTO> timesheetPageDTO = getEmployeePageDTO(employeeResponse);
                timesheetPageDTO.ifPresent(result::add);
            }
        }

        return result;
    }

    private Optional<EmployeePageDTO> getEmployeePageDTO(EmployeeResponse employeeResponse) {
        EmployeePageDTO employeePageDTO = new EmployeePageDTO();
        employeePageDTO.setEmployeeId(String.valueOf(employeeResponse.getId()));
        employeePageDTO.setEmployeeName(employeeResponse.getName());
        employeePageDTO.setEmployeeSurname(employeeResponse.getSurname());
        return Optional.of(employeePageDTO);
    }

}
