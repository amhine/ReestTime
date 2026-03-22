package com.RestTime.RestTime.controller;

import com.RestTime.RestTime.dto.*;
import com.RestTime.RestTime.service.CongeService;
import com.RestTime.RestTime.service.EmployeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestPart;

import java.util.List;

@RestController
@RequestMapping("/api/employes")
@RequiredArgsConstructor
public class EmployeController {

    private final EmployeService employeService;


    @GetMapping("/{userId}/solde")
    public ResponseEntity<SoldeCongeDTO> getSoldeConges(@PathVariable(name = "userId") Long userId) {
        return ResponseEntity.ok(employeService.getSoldeConges(userId));
    }


    @DeleteMapping("/{userId}/demandes/{demandeId}")
    public ResponseEntity<Void> annulerDemande(
            @PathVariable(name = "userId") Long userId,
            @PathVariable(name = "demandeId") Long demandeId) {
        employeService.annulerDemande(demandeId, userId);
        return ResponseEntity.noContent().build();
    }




    private final CongeService congeService;

    @PostMapping(value = "/conges/{userId}", consumes = "multipart/form-data")
    public ResponseEntity<DemandeCongeResponseDTO> soumettreDemande(
            @PathVariable Long userId,
            @RequestPart("dto") DemandeCongeCreateDTO dto,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        return ResponseEntity.ok(congeService.soumettreDemande(userId, dto, file));
    }

    @PutMapping(value = "/conges/{userId}/{demandeId}", consumes = "multipart/form-data")
    public ResponseEntity<DemandeCongeResponseDTO> modifierDemande(
            @PathVariable Long userId,
            @PathVariable Long demandeId,
            @RequestPart("dto") DemandeCongeCreateDTO dto,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        return ResponseEntity.ok(congeService.modifierDemande(demandeId, userId, dto, file));
    }

    @GetMapping("/{userId}/mesdemandes")
    public ResponseEntity<List<DemandeCongeResponseDTO>> getMesDemandes(@PathVariable(name = "userId") Long userId) {
        return ResponseEntity.ok(congeService.getMesDemandes(userId));
    }

    @PutMapping("/{userId}/profil")
    public ResponseEntity<UserResponseDTO> updateProfile(
            @PathVariable Long userId,
            @RequestBody UpdateUserRequestDTO dto) {
        return ResponseEntity.ok(employeService.updateProfile(userId, dto, null));
    }
}