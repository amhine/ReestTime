package com.RestTime.RestTime.model.entity;
import com.RestTime.RestTime.model.enumeration.TypeConge;
import jakarta.persistence.*;
import lombok.*;
import com.RestTime.RestTime.model.enumeration.StatutDemande;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "demandeconge")
@Builder
public class DemandeConge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dateDebut;
    private LocalDate dateFin;
    private Integer nombreJours;
    private String motif;
    private LocalDate dateSoumission;
    private String pieceJointe;

    @Enumerated(EnumType.STRING)
    private StatutDemande statut;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    @Enumerated(EnumType.STRING)
    private TypeConge type;

    @OneToMany(mappedBy = "demandeConge", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Historique> historiques;

}
