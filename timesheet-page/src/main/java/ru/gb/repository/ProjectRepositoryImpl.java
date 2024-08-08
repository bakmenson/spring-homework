package ru.gb.repository;

import jakarta.validation.constraints.NotNull;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import ru.gb.client.ProjectResponse;
import ru.gb.dto.ProjectPageDTO;

import java.util.*;

@Component
public class ProjectRepositoryImpl implements ProjectRepository {

    @Override
    public List<ProjectPageDTO> findProjects(String uri, Long id, RestClient restClient) {
        List<ProjectPageDTO> result = new ArrayList<>();

        Optional<List<ProjectResponse>> projects;

        try {
            projects = Optional.ofNullable(restClient.get()
                    .uri(uri + id + "/projects")
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<ProjectResponse>>() {
                    }));

        } catch (HttpClientErrorException.NotFound e) {
            return result;
        }

        if (projects.isPresent()) {
            for (ProjectResponse projectResponse : projects.get()) {
                Optional<ProjectPageDTO> projectPageDTO = getProjectPageDTO(projectResponse);
                projectPageDTO.ifPresent(result::add);
            }
        }

        return result;
    }

    private Optional<ProjectPageDTO> getProjectPageDTO(@NotNull ProjectResponse response) {
        ProjectPageDTO projectPageDTO = new ProjectPageDTO();
        projectPageDTO.setProjectId(String.valueOf(response.getId()));
        return Optional.of(projectPageDTO);
    }

}
