package ru.gb.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import ru.gb.client.TimesheetResponse;
import ru.gb.dto.TimesheetPageDTO;
import ru.gb.repository.TimesheetsRepository;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@RequiredArgsConstructor
@Service
public class TimesheetServiceImpl implements TimesheetService {

    private final DiscoveryClient discoveryClient;
    private final TimesheetsRepository repository;

    private RestClient restClient() {
        List<ServiceInstance> instances = discoveryClient.getInstances("timesheet-rest");
        int instancesCount = instances.size();
        int instanceIndex = ThreadLocalRandom.current().nextInt(0, instancesCount);

        ServiceInstance instance = instances.get(instanceIndex);
        String uri = "http://" + instance.getHost() + ":" + instance.getPort();
        return RestClient.create(uri);
    }

    @Override
    public List<TimesheetPageDTO> findAll() {
        List<TimesheetPageDTO> result = new ArrayList<>();

        Optional<List<TimesheetResponse>> timesheets;

        try {
            timesheets = Optional.ofNullable(restClient().get()
                    .uri("/timesheets")
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<TimesheetResponse>>() {
                    }));
        } catch (HttpClientErrorException.NotFound e) {
            return List.of();
        }

        if (timesheets.isPresent()) {
            for (TimesheetResponse timesheetResponse : timesheets.get()) {
                Optional<TimesheetPageDTO> timesheetPageDTO = repository.getTimesheetPageDTO(timesheetResponse, restClient());
                timesheetPageDTO.ifPresent(result::add);
            }

            return result;
        }

        return List.of();
    }

    @Override
    public Optional<TimesheetPageDTO> findById(Long id) {
        return repository.getTimesheetPageDTO(id, restClient());
    }

}
