package ru.gb.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;


@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "timesheets")
public class Timesheet {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column
    private Long projectId;

    @Column
    private int minutes;

    @Column
    private LocalDate createdAt;

    @Column
    private Long employeeId;

}
