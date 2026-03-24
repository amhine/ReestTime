package com.RestTime.RestTime.dto;

import com.RestTime.RestTime.model.enumeration.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private Double soldeConges;
    private Role role;
    private String telephone;
    private String adresse;
    private String departement;
}