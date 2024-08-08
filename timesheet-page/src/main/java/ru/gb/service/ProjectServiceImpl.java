package ru.gb.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import ru.gb.client.ProjectResponse;
import ru.gb.dto.EmployeePageDTO;
import ru.gb.dto.ProjectPageDTO;
import ru.gb.dto.TimesheetPageDTO;
import ru.gb.repository.EmployeeRepository;
import ru.gb.repository.TimesheetsRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@RequiredArgsConstructor
@Service
public class ProjectServiceImpl implements ProjectService {

    private final DiscoveryClient discoveryClient;
    private final TimesheetsRepository timesheetsRepository;
    private final EmployeeRepository employeeRepository;

    private RestClient restClient() {
        List<ServiceInstance> instances = discoveryClient.getInstances("timesheet-rest");
        int instancesCount = instances.size();
        int instanceIndex = ThreadLocalRandom.current().nextInt(0, instancesCount);

        ServiceInstance instance = instances.get(instanceIndex);
        String uri = "http://" + instance.getHost() + ":" + instance.getPort();
        return RestClient.create(uri);
    }

    @Override
    public List<ProjectPageDTO> findAll() {
        List<ProjectPageDTO> result = new ArrayList<>();

        Optional<List<ProjectResponse>> projectResponses;

        try {
            projectResponses = Optional.ofNullable(restClient().get()
                    .uri("/projects")
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<ProjectResponse>>() {
                    }));
        } catch (HttpClientErrorException.NotFound e) {
            return List.of();
        }

        if (projectResponses.isPresent()) {
            for (ProjectResponse response : projectResponses.get()) {
                ProjectPageDTO projectPageDTO = new ProjectPageDTO();
                projectPageDTO.setProjectId(String.valueOf(response.getId()));
                projectPageDTO.setProjectName(response.getName());
                result.add(projectPageDTO);
            }

            return result;
        }

        return List.of();
    }

    @Override
    public Optional<ProjectPageDTO> findById(Long id) {
        try {
            Optional<ProjectResponse> projectResponse = Optional.ofNullable(restClient().get()
                    .uri("/projects/" + id)
                    .retrieve()
                    .body(ProjectResponse.class));

            ProjectPageDTO projectPageDTO = new ProjectPageDTO();

            if (projectResponse.isPresent()) {
                projectPageDTO.setProjectId(String.valueOf(projectResponse.get().getId()));
                projectPageDTO.setProjectName(projectResponse.get().getName());
            }

            return Optional.of(projectPageDTO);
        } catch (HttpClientErrorException.NotFound e) {
            return Optional.empty();
        }
    }

    @Override
    public List<TimesheetPageDTO> findProjectTimesheets(Long id) {
        return timesheetsRepository.findTimesheets("/projects/", id, restClient());
    }

    @Override
    public Set<EmployeePageDTO> findProjectEmployees(Long id) {
        return employeeRepository.findEmployees("/projects/", id, restClient());
    }

}
