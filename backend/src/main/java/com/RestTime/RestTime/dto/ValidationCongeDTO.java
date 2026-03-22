package com.RestTime.RestTime.dto;

import com.RestTime.RestTime.model.enumeration.StatutDemande;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ValidationCongeDTO {
    private StatutDemande statut;
    private String details;
}