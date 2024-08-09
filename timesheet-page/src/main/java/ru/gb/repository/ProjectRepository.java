package ru.gb.repository;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.client.RestClient;
import ru.gb.dto.ProjectPageDTO;

import java.util.List;

public interface ProjectRepository {

    List<ProjectPageDTO> findProjects(@NotNull String uri, @NotNull Long id, @NotNull RestClient restClient);

}
