package com.RestTime.RestTime.model.entity;
import jakarta.persistence.*;
import lombok.*;
import com.RestTime.RestTime.model.enumeration.Role;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String prenom;
    @Column(unique = true)
    private String email;
    private String motDePasse;
    private Double soldeConges;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String telephone;
    private String adresse;
    private String departement;



    @OneToMany(mappedBy = "user")
    private List<DemandeConge> demandes;

    @OneToMany(mappedBy = "user")
    private List<Notification> notifications;

    @Column(name = "reset_token")
    private String resetToken;

    @Column(name = "reset_token_creation_date")
    private LocalDateTime resetTokenCreationDate;
}
