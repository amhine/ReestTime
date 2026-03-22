package com.RestTime.RestTime.service;

import com.RestTime.RestTime.dto.AnnouncementCreateDTO;
import com.RestTime.RestTime.dto.AnnouncementDTO;
import com.RestTime.RestTime.mapper.AnnouncementMapper;
import com.RestTime.RestTime.model.entity.Announcement;
import com.RestTime.RestTime.model.entity.Notification;
import com.RestTime.RestTime.model.entity.User;
import com.RestTime.RestTime.model.enumeration.TypeNotification;
import com.RestTime.RestTime.repository.AnnouncementRepository;
import com.RestTime.RestTime.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class AnnouncementService {

    private final AnnouncementRepository announcementRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final AnnouncementMapper announcementMapper;

    @Transactional
    public AnnouncementDTO createAnnouncement(AnnouncementCreateDTO dto) {
        Announcement announcement = Announcement.builder()
                .titre(dto.getTitre())
                .contenu(dto.getContenu())
                .dateCreation(LocalDateTime.now())
                .build();
        Announcement savedAnnouncement = announcementRepository.save(announcement);

        List<User> employees = userRepository.findByRole(com.RestTime.RestTime.model.enumeration.Role.EMPLOYE);

        for (User user : employees) {
            try {
                Notification notification = Notification.builder()
                        .user(user)
                        .titre("Nouvelle Annonce")
                        .message(savedAnnouncement.getTitre())
                        .type(TypeNotification.ANNONCE_PUBLIEE)
                        .dateEnvoi(LocalDateTime.now())
                        .lue(false)
                        .build();

                notificationService.saveAndBroadcastNotification(notification);
            } catch (Exception e) {
                System.err.println("Erreur notification pour l'employé " + user.getEmail());
            }
        }

        return announcementMapper.toDTO(savedAnnouncement);
    }
    @Transactional
    public AnnouncementDTO updateAnnouncement(Long id, AnnouncementCreateDTO dto) {
        Announcement announcement = announcementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Annonce non trouvée"));

        announcement.setTitre(dto.getTitre());
        announcement.setContenu(dto.getContenu());

        Announcement updated = announcementRepository.save(announcement);
        return announcementMapper.toDTO(updated);
    }

    public List<AnnouncementDTO> getAllAnnouncements() {
        return announcementRepository.findAllByOrderByDateCreationDesc()
                .stream()
                .map(announcementMapper::toDTO)
                .collect(Collectors.toList());
    }

    public void deleteAnnouncement(Long id) {
        announcementRepository.deleteById(id);
    }
}