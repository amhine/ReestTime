package com.RestTime.RestTime.service.impl;

import com.RestTime.RestTime.dto.SoldeCongeDTO;
import com.RestTime.RestTime.dto.UserResponseDTO;
import com.RestTime.RestTime.dto.UpdateUserRequestDTO;
import com.RestTime.RestTime.model.entity.DemandeConge;
import com.RestTime.RestTime.model.entity.User;
import com.RestTime.RestTime.model.enumeration.StatutDemande;
import com.RestTime.RestTime.repository.DemandeCongeRepository;
import com.RestTime.RestTime.repository.HistoriqueRepository;
import com.RestTime.RestTime.repository.UserRepository;
import com.RestTime.RestTime.service.EmployeService;
import com.RestTime.RestTime.mapper.UtilisateurMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder; // <--- AJOUTER
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeServiceImpl implements EmployeService {

    private final UserRepository userRepository;
    private final DemandeCongeRepository demandeCongeRepository;
    private final HistoriqueRepository historiqueRepository;
    private final UtilisateurMapper utilisateurMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public SoldeCongeDTO getSoldeConges(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        List<DemandeConge> demandes = demandeCongeRepository.findByUserOrderByIdDesc(user);

        double soldeUtilise = demandes.stream()
                .filter(d -> d.getStatut() == StatutDemande.VALIDEE)
                .mapToDouble(DemandeConge::getNombreJours)
                .sum();

        double soldeRestant = (user.getSoldeConges() != null) ? user.getSoldeConges() : 0.0;
        double soldeActuel = soldeRestant + soldeUtilise;

        return SoldeCongeDTO.builder()
                .soldeActuel(soldeActuel)
                .soldeUtilise(soldeUtilise)
                .soldeRestant(soldeRestant)
                .build();
    }

    @Override
    @Transactional
    public void annulerDemande(Long demandeId, Long userId) {
        DemandeConge demande = demandeCongeRepository.findById(demandeId)
                .orElseThrow(() -> new RuntimeException("Demande introuvable"));

        if (!demande.getUser().getId().equals(userId)) {
            throw new RuntimeException("Vous n'êtes pas autorisé à annuler cette demande.");
        }

        if (!demande.getStatut().equals(StatutDemande.EN_ATTENTE)) {
            throw new RuntimeException("Impossible d'annuler.");
        }

        historiqueRepository.deleteByDemandeConge(demande);
        demandeCongeRepository.delete(demande);
    }

    @Override
    @Transactional
    public UserResponseDTO updateProfile(Long userId, UpdateUserRequestDTO dto, MultipartFile photo) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        if (dto.getTelephone() != null) user.setTelephone(dto.getTelephone());
        if (dto.getAdresse() != null) user.setAdresse(dto.getAdresse());

        if (dto.getMotDePasse() != null && !dto.getMotDePasse().isEmpty()) {
            user.setMotDePasse(passwordEncoder.encode(dto.getMotDePasse()));
        }

        return utilisateurMapper.toResponseDTO(userRepository.save(user));
    }
}