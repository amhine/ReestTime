package com.RestTime.RestTime.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequestDTO {
    private String nom;
    private String prenom;
    private String email;
    private Double soldeConges;
    private String telephone;
    private String adresse;
    private String motDePasse;
    private String poste;
    private String departement;
}