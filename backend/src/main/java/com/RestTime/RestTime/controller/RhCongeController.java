package com.RestTime.RestTime.controller;

import com.RestTime.RestTime.dto.DemandeCongeResponseDTO;
import com.RestTime.RestTime.dto.HistoriqueResponseDTO;
import com.RestTime.RestTime.dto.StatistiquesRhDTO;
import com.RestTime.RestTime.dto.ValidationCongeDTO;
import com.RestTime.RestTime.service.CongeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rh/conges")
@RequiredArgsConstructor
public class RhCongeController {

    private final CongeService congeService;

    @GetMapping("/enattente")
    public ResponseEntity<List<DemandeCongeResponseDTO>> getDemandesEnAttente() {
        return ResponseEntity.ok(congeService.getDemandesEnAttente());
    }

    @PutMapping("/{demandeId}/traiter")
    public ResponseEntity<DemandeCongeResponseDTO> traiterDemande(
            @PathVariable(name = "demandeId") Long demandeId,
            @RequestBody ValidationCongeDTO dto) {
        return ResponseEntity.ok(congeService.traiterDemande(demandeId, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DemandeCongeResponseDTO> getDemandeById(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(congeService.getDemandeById(id));
    }

    @GetMapping("/statistiques")
    public ResponseEntity<StatistiquesRhDTO> getStatistiques() {
        return ResponseEntity.ok(congeService.getStatistiques());
    }

    @GetMapping("/historique")
    public ResponseEntity<List<HistoriqueResponseDTO>> getHistoriqueGlobal() {
        return ResponseEntity.ok(congeService.getHistoriqueGlobal());
    }

    @GetMapping("/all")
    public ResponseEntity<List<DemandeCongeResponseDTO>> getAllDemandes() {
        return ResponseEntity.ok(congeService.getAllDemandes());
    }
}