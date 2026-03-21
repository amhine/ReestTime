package com.RestTime.RestTime.service;

import com.RestTime.RestTime.dto.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface CongeService {
    DemandeCongeResponseDTO soumettreDemande(Long userId, DemandeCongeCreateDTO dto, MultipartFile file);

    DemandeCongeResponseDTO modifierDemande(Long demandeId, Long userId, DemandeCongeCreateDTO dto, MultipartFile file);

    DemandeCongeResponseDTO traiterDemande(Long demandeId, ValidationCongeDTO dto);

    List<DemandeCongeResponseDTO> getMesDemandes(Long userId);

    List<DemandeCongeResponseDTO> getDemandesEnAttente();
    List<DemandeCongeResponseDTO> getAllDemandes();

    DemandeCongeResponseDTO getDemandeById(Long id);

    StatistiquesRhDTO getStatistiques();

    List<HistoriqueResponseDTO> getHistoriqueGlobal();
}