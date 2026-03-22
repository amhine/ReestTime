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
public class CreateUserRequestDTO {
    private String nom;
    private String prenom;
    private String email;
    private String motDePasse;
    private Double soldeConges;
    private Role role;
    private String departement;
}