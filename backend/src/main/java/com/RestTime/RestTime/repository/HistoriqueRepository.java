package com.RestTime.RestTime.repository;

import com.RestTime.RestTime.model.entity.DemandeConge;
import com.RestTime.RestTime.model.entity.Historique;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoriqueRepository extends JpaRepository<Historique, Long> {
    void deleteByDemandeConge(DemandeConge demandeConge);
    List<Historique> findAllByOrderByDateActionDesc();
}
