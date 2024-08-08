package ru.gb.client;

import lombok.Data;

@Data
public class EmployeeResponse {

    private Long id;
    private String name;
    private String surname;
//    private Set<Project> employeeProjects;

}
