package com.RestTime.RestTime.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceDTO {
    private Long id;
    private LocalDate date;
    private LocalTime heureEntree;
    private LocalTime heureSortie;
    private Double heuresTravaillees;
    private Boolean estEnRetard;
    private Long minutesRetard;
    private Long userId;
    private String prenom;
    private String nom;
}
