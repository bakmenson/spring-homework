package ru.gb.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import ru.gb.client.EmployeeResponse;
import ru.gb.dto.EmployeePageDTO;
import ru.gb.dto.ProjectPageDTO;
import ru.gb.dto.TimesheetPageDTO;
import ru.gb.repository.ProjectRepository;
import ru.gb.repository.TimesheetsRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@RequiredArgsConstructor
@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final DiscoveryClient discoveryClient;
    private final TimesheetsRepository timesheetsRepository;
    private final ProjectRepository projectRepository;

    private RestClient restClient() {
        List<ServiceInstance> instances = discoveryClient.getInstances("timesheet-rest");
        int instancesCount = instances.size();
        int instanceIndex = ThreadLocalRandom.current().nextInt(0, instancesCount);

        ServiceInstance instance = instances.get(instanceIndex);
        String uri = "http://" + instance.getHost() + ":" + instance.getPort();
        return RestClient.create(uri);
    }

    @Override
    public List<EmployeePageDTO> findAll() {
        List<EmployeePageDTO> result = new ArrayList<>();

        Optional<List<EmployeeResponse>> employees;

        try {
            employees = Optional.ofNullable(restClient().get()
                    .uri("/employees")
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<EmployeeResponse>>() {
                    }));

        } catch (HttpClientErrorException.NotFound e) {
            return result;
        }

        if (employees.isPresent()) {
            for (EmployeeResponse employeeResponse : employees.get()) {
                EmployeePageDTO employeePageDTO = new EmployeePageDTO();
                employeePageDTO.setEmployeeId(String.valueOf(employeeResponse.getId()));
                employeePageDTO.setEmployeeName(employeeResponse.getName());
                employeePageDTO.setEmployeeSurname(employeeResponse.getSurname());
                result.add(employeePageDTO);
            }
        }

        return result;
    }

    @Override
    public Optional<EmployeePageDTO> findById(Long id) {
        try {
            Optional<EmployeeResponse> employee = Optional.ofNullable(restClient().get()
                    .uri("/employees/" + id)
                    .retrieve()
                    .body(EmployeeResponse.class));

            EmployeePageDTO employeePageDTO = new EmployeePageDTO();

            if (employee.isPresent()) {
                employeePageDTO.setEmployeeId(String.valueOf(employee.get().getId()));
                employeePageDTO.setEmployeeName(employee.get().getName());
                employeePageDTO.setEmployeeSurname(employee.get().getSurname());
            }

            return Optional.of(employeePageDTO);

        } catch (HttpClientErrorException.NotFound e) {
            return Optional.empty();
        }
    }

    @Override
    public List<TimesheetPageDTO> findByEmployeeId(Long id) {
        return timesheetsRepository.findTimesheets("/employees/", id, restClient());
    }

    @Override
    public List<ProjectPageDTO> findEmployeeProjects(Long id) {
        return projectRepository.findProjects("/employees/", id, restClient());
    }

}
