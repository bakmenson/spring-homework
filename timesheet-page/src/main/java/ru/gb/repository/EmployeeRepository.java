package ru.gb.repository;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.client.RestClient;
import ru.gb.dto.EmployeePageDTO;

import java.util.Set;

public interface EmployeeRepository {

    Set<EmployeePageDTO> findEmployees(@NotNull String uri, @NotNull Long id, @NotNull RestClient restClient);

}
