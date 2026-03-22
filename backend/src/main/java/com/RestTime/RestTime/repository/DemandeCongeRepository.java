package com.RestTime.RestTime.repository;

import com.RestTime.RestTime.model.entity.DemandeConge;
import com.RestTime.RestTime.model.entity.User;
import com.RestTime.RestTime.model.enumeration.StatutDemande;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface DemandeCongeRepository extends JpaRepository<DemandeConge, Long> {
    List<DemandeConge> findByUserOrderByIdDesc(User user);
    List<DemandeConge> findByStatutOrderByDateSoumissionDesc(StatutDemande statut);
    List<DemandeConge> findByStatutOrderByIdDesc(StatutDemande statut);
    long countByStatut(StatutDemande statut);
    boolean existsByUserAndStatut(User user, StatutDemande statut);

    boolean existsByUserAndDateDebutLessThanEqualAndDateFinGreaterThanEqual(User user, LocalDate fin, LocalDate debut);
}