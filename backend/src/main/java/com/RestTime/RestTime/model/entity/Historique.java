package com.RestTime.RestTime.model.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "historique")
@Builder
public class Historique {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String action;
    private LocalDateTime dateAction;
    private String details;

    @ManyToOne
    @JoinColumn(name = "demande_conge_id")
    private DemandeConge demandeConge;
}
