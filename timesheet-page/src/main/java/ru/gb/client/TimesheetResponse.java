package ru.gb.client;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TimesheetResponse {

    private Long id;
    private Long projectId;
    private int minutes;
    private LocalDate createdAt;
    private Long employeeId;

}
