package com.RestTime.RestTime.model.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "attendances")
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;
    private LocalTime heureEntree;
    private LocalTime heureSortie;
    private Double heuresTravaillees;
    private Boolean estEnRetard;
    private Long minutesRetard;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}