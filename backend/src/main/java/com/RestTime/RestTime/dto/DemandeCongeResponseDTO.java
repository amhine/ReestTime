package com.RestTime.RestTime.dto;

import com.RestTime.RestTime.model.enumeration.StatutDemande;
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
public class DemandeCongeResponseDTO {

    private Long id;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private int nombreJours;
    private String motif;
    private LocalDate dateSoumission;
    private StatutDemande statut;
    private TypeConge type;
    private String pieceJointe;

    private Long userId;
    private String nom;
    private String prenom;
}
