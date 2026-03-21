package com.RestTime.RestTime.service.impl;

import com.RestTime.RestTime.dto.*;
import com.RestTime.RestTime.mapper.DemandeCongeMapper;
import com.RestTime.RestTime.mapper.HistoriqueMapper;
import com.RestTime.RestTime.model.entity.DemandeConge;
import com.RestTime.RestTime.model.entity.Historique;
import com.RestTime.RestTime.model.entity.Notification;
import com.RestTime.RestTime.model.entity.User;
import com.RestTime.RestTime.model.enumeration.StatutDemande;
import com.RestTime.RestTime.model.enumeration.TypeNotification;
import com.RestTime.RestTime.repository.*;
import com.RestTime.RestTime.service.FileService;
import com.RestTime.RestTime.service.NotificationService;
import com.RestTime.RestTime.service.CongeService;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CongeServiceImpl implements CongeService {

    private final DemandeCongeRepository demandeCongeRepository;
    private final UserRepository userRepository;
    private final HistoriqueRepository historiqueRepository;
    private final DemandeCongeMapper demandeCongeMapper;
    private final HistoriqueMapper historiqueMapper;
    private final NotificationService notificationService;
    private final FileService fileService;
    private final AttendanceRepository attendanceRepository;

    @Override
    @Transactional
    public DemandeCongeResponseDTO modifierDemande(Long demandeId, Long userId, DemandeCongeCreateDTO dto,
            MultipartFile file) {
        DemandeConge demande = demandeCongeRepository.findById(demandeId)
                .orElseThrow(() -> new RuntimeException("Demande introuvable"));

        if (!demande.getUser().getId().equals(userId)) {
            throw new RuntimeException("Vous n'êtes pas autorisé à modifier cette demande");
        }

        if (demande.getStatut() != StatutDemande.EN_ATTENTE) {
            throw new RuntimeException("Seulement les demandes en attente peuvent être modifiées");
        }

        LocalDate today = LocalDate.now();
        if (dto.getDateDebut().isBefore(today)) {
            throw new RuntimeException("Date de début ne peut pas être dans le passé");
        }

        int nombreJours = calculerNombreJours(dto.getDateDebut(), dto.getDateFin());
        if (nombreJours <= 0) {
            throw new RuntimeException("Dates invalides");
        }

        // Logic for balances (similar to soumettreDemande)
        switch (dto.getType()) {
            case ANNUEL:
                if (nombreJours > 15)
                    throw new RuntimeException("Congé annuel max 15 jours");
                if (demande.getUser().getSoldeConges() < nombreJours)
                    throw new RuntimeException("Solde insuffisant");
                break;
            case MALADIE:
                if (nombreJours > 30)
                    throw new RuntimeException("Congé maladie max 30 jours");
                break;
            case EXCEPTIONNEL:
                if (nombreJours > 5)
                    throw new RuntimeException("Congé exceptionnel max 5 jours");
                break;
            case FORMATION:
                if (nombreJours > 30)
                    throw new RuntimeException("Congé formation max 30 jours");
                break;
        }

        demande.setType(dto.getType());
        demande.setDateDebut(dto.getDateDebut());
        demande.setDateFin(dto.getDateFin());
        demande.setMotif(dto.getMotif());
        demande.setNombreJours(nombreJours);

        if (file != null && !file.isEmpty()) {
            try {
                if (demande.getPieceJointe() != null) {
                    fileService.deleteFile(demande.getPieceJointe());
                }
                String filePath = fileService.saveFile(file, "justificatifs");
                demande.setPieceJointe(filePath);
            } catch (IOException e) {
                throw new RuntimeException("Erreur lors de l'enregistrement du fichier");
            }
        }

        demande = demandeCongeRepository.save(demande);
        enregistrerHistorique(demande, "Modification", "Demande modifiée par l'employé");

        return demandeCongeMapper.toResponseDTO(demande);
    }

    @Override
    @Transactional
    public DemandeCongeResponseDTO soumettreDemande(Long userId, DemandeCongeCreateDTO dto, MultipartFile file) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        LocalDate today = LocalDate.now();

        if (dto.getDateDebut().isBefore(today)) {
            throw new RuntimeException("Date de début ne peut pas être dans le passé");
        }

        int nombreJours = calculerNombreJours(dto.getDateDebut(), dto.getDateFin());

        if (nombreJours <= 0) {
            throw new RuntimeException("Dates invalides");
        }

        boolean hasPending = demandeCongeRepository
                .existsByUserAndStatut(user, StatutDemande.EN_ATTENTE);

        if (hasPending) {
            throw new RuntimeException("Vous avez déjà une demande en attente");
        }

        boolean overlap = demandeCongeRepository
                .existsByUserAndDateDebutLessThanEqualAndDateFinGreaterThanEqual(
                        user,
                        dto.getDateFin(),
                        dto.getDateDebut());

        if (overlap) {
            throw new RuntimeException("Dates chevauchent un congé existant");
        }

        switch (dto.getType()) {

            case ANNUEL:

                if (nombreJours > 15) {
                    throw new RuntimeException("Congé annuel max 15 jours");
                }

                if (user.getSoldeConges() < nombreJours) {
                    throw new RuntimeException("Solde insuffisant");
                }
                break;

            case MALADIE:

                if (nombreJours > 30) {
                    throw new RuntimeException("Congé maladie max 30 jours");
                }
                break;

            case EXCEPTIONNEL:

                if (nombreJours > 5) {
                    throw new RuntimeException("Congé exceptionnel max 5 jours");
                }
                break;

            case FORMATION:

                if (nombreJours > 30) {
                    throw new RuntimeException("Congé formation max 30 jours");
                }
                break;
        }

        DemandeConge demande = demandeCongeMapper.toEntity(dto);

        demande.setNombreJours(nombreJours);
        demande.setDateSoumission(today);
        demande.setStatut(StatutDemande.EN_ATTENTE);
        demande.setUser(user);
        demande.setType(dto.getType());

        if (file != null && !file.isEmpty()) {
            try {
                String filePath = fileService.saveFile(file, "justificatifs");
                demande.setPieceJointe(filePath);
            } catch (IOException e) {
                throw new RuntimeException("Erreur lors de l'enregistrement du fichier");
            }
        }

        demande = demandeCongeRepository.save(demande);

        enregistrerHistorique(demande, "Soumission", "Demande soumise par l'employé");

        List<User> rhUsers = userRepository.findByRole(com.RestTime.RestTime.model.enumeration.Role.RH);
        for (User rh : rhUsers) {
            Notification notifRh = Notification.builder()
                    .user(rh)
                    .titre("Nouvelle demande de congé")
                    .message("Une nouvelle demande de " + user.getPrenom() + " " + user.getNom() + " est en attente.")
                    .type(TypeNotification.DEMANDE_SOUMISE)
                    .dateEnvoi(LocalDateTime.now())
                    .lue(false)
                    .build();
            notificationService.saveAndBroadcastNotification(notifRh);
        }

        return demandeCongeMapper.toResponseDTO(demande);

    }

    @Override
    @Transactional
    public DemandeCongeResponseDTO traiterDemande(Long demandeId, ValidationCongeDTO dto) {
        DemandeConge demande = demandeCongeRepository.findById(demandeId)
                .orElseThrow(() -> new RuntimeException("Demande introuvable"));

        if (demande.getStatut() != StatutDemande.EN_ATTENTE) {
            throw new RuntimeException("La demande n'est plus en attente.");
        }

        if (dto.getStatut() != StatutDemande.VALIDEE && dto.getStatut() != StatutDemande.REFUSEE) {
            throw new RuntimeException("Statut invalide pour traitement RH");
        }

        demande.setStatut(dto.getStatut());

        if (dto.getStatut() == StatutDemande.VALIDEE) {
            User user = demande.getUser();
            double nouveauSolde = user.getSoldeConges() - demande.getNombreJours();
            if (nouveauSolde < 0) {
                throw new RuntimeException("Solde insuffisant");
            }
            user.setSoldeConges(nouveauSolde);
            userRepository.save(user);

            Notification notification = Notification.builder()
                    .user(user)
                    .titre("Demande validée")
                    .message("Votre demande de congé a été validée.")
                    .type(TypeNotification.DEMANDE_VALIDEE)
                    .dateEnvoi(LocalDateTime.now())
                    .lue(false)
                    .build();
            notificationService.saveAndBroadcastNotification(notification);
        }

        if (dto.getStatut() == StatutDemande.REFUSEE) {
            User user = demande.getUser();

            Notification notification = Notification.builder()
                    .user(user)
                    .titre("Demande refusée")
                    .message("Votre demande de congé a été refusée.")
                    .type(TypeNotification.DEMANDE_REFUSEE)
                    .dateEnvoi(LocalDateTime.now())
                    .lue(false)
                    .build();
            notificationService.saveAndBroadcastNotification(notification);
        }

        demande = demandeCongeRepository.save(demande);

        enregistrerHistorique(
                demande,
                "Traitement RH : " + dto.getStatut().name(),
                dto.getDetails());

        return demandeCongeMapper.toResponseDTO(demande);
    }

    @Override
    public List<DemandeCongeResponseDTO> getMesDemandes(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        return demandeCongeRepository.findByUserOrderByIdDesc(user)
                .stream()
                .map(demandeCongeMapper::toResponseDTO)
                .toList();
    }

    @Override
    public List<DemandeCongeResponseDTO> getDemandesEnAttente() {
        return demandeCongeRepository.findByStatutOrderByIdDesc(StatutDemande.EN_ATTENTE)
                .stream()
                .map(demandeCongeMapper::toResponseDTO)
                .toList();
    }

    @Override
    public List<DemandeCongeResponseDTO> getAllDemandes() {
        return demandeCongeRepository.findAll().stream()
                .sorted((d1, d2) -> d2.getId().compareTo(d1.getId()))
                .map(demandeCongeMapper::toResponseDTO)
                .toList();
    }

    private int calculerNombreJours(LocalDate debut, LocalDate fin) {
        if (fin.isBefore(debut)) {
            return 0;
        }
        long jours = ChronoUnit.DAYS.between(debut, fin) + 1;
        return (int) jours;
    }

    private void enregistrerHistorique(DemandeConge demande, String action, String details) {
        Historique historique = Historique.builder()
                .action(action)
                .dateAction(LocalDateTime.now())
                .details(details)
                .demandeConge(demande)
                .build();
        historiqueRepository.save(historique);
    }

    @Override
    public DemandeCongeResponseDTO getDemandeById(Long id) {
        DemandeConge demande = demandeCongeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Demande introuvable avec l'id : " + id));
        return demandeCongeMapper.toResponseDTO(demande);
    }

    @Override
    public StatistiquesRhDTO getStatistiques() {
        long total = demandeCongeRepository.count();
        long approuvees = demandeCongeRepository.countByStatut(StatutDemande.VALIDEE);
        long refusees = demandeCongeRepository.countByStatut(StatutDemande.REFUSEE);
        long enAttente = demandeCongeRepository.countByStatut(StatutDemande.EN_ATTENTE);

        // Calculate absences
        long totalEmployees = userRepository.findByRole(com.RestTime.RestTime.model.enumeration.Role.EMPLOYE).size();
        long checkedInToday = attendanceRepository.countByDate(LocalDate.now());
        long totalAbsences = Math.max(0, totalEmployees - checkedInToday);

        return StatistiquesRhDTO.builder()
                .totalDemandes(total)
                .demandesApprouvees(approuvees)
                .demandesRefusees(refusees)
                .demandesEnAttente(enAttente)
                .totalAbsences(totalAbsences)
                .tauxApprobation(total > 0 ? (approuvees * 100.0 / total) : 0)
                .build();
    }

    @Override
    public List<HistoriqueResponseDTO> getHistoriqueGlobal() {
        return historiqueRepository.findAllByOrderByDateActionDesc().stream()
                .map(historiqueMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

}