package com.RestTime.RestTime.dto;

import com.RestTime.RestTime.model.enumeration.TypeConge;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DemandeCongeCreateDTO {
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String motif;
    private TypeConge type;
}