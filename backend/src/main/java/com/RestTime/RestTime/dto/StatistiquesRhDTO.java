package com.RestTime.RestTime.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatistiquesRhDTO {
    private long totalDemandes;
    private long demandesApprouvees;
    private long demandesRefusees;
    private long demandesEnAttente;
    private long totalAbsences;
    private double tauxApprobation;
}
