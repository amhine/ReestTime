package com.RestTime.RestTime.service.impl;

import com.RestTime.RestTime.dto.DemandeCongeCreateDTO;
import com.RestTime.RestTime.dto.DemandeCongeResponseDTO;
import com.RestTime.RestTime.dto.ValidationCongeDTO;
import com.RestTime.RestTime.mapper.DemandeCongeMapper;
import com.RestTime.RestTime.mapper.HistoriqueMapper;
import com.RestTime.RestTime.model.entity.DemandeConge;
import com.RestTime.RestTime.model.entity.User;
import com.RestTime.RestTime.model.enumeration.StatutDemande;
import com.RestTime.RestTime.model.enumeration.TypeConge;
import com.RestTime.RestTime.repository.*;
import com.RestTime.RestTime.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CongeServiceImplTest {

    @Mock
    private DemandeCongeRepository demandeCongeRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private HistoriqueRepository historiqueRepository;
    @Mock
    private DemandeCongeMapper demandeCongeMapper;
    @Mock
    private HistoriqueMapper historiqueMapper;
    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private CongeServiceImpl congeService;

    private User user;
    private DemandeConge demande;
    private DemandeCongeCreateDTO createDTO;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setSoldeConges(20.0);
        user.setPrenom("John");
        user.setNom("Doe");

        createDTO = new DemandeCongeCreateDTO();
        createDTO.setDateDebut(LocalDate.now().plusDays(1));
        createDTO.setDateFin(LocalDate.now().plusDays(3));
        createDTO.setType(TypeConge.ANNUEL);
        createDTO.setMotif("Vacances");

        demande = new DemandeConge();
        demande.setId(1L);
        demande.setUser(user);
        demande.setStatut(StatutDemande.EN_ATTENTE);
        demande.setNombreJours(3);
    }

    @Test
    void soumettreDemande_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(demandeCongeRepository.existsByUserAndStatut(user, StatutDemande.EN_ATTENTE)).thenReturn(false);
        when(demandeCongeRepository.existsByUserAndDateDebutLessThanEqualAndDateFinGreaterThanEqual(any(), any(), any())).thenReturn(false);
        when(demandeCongeMapper.toEntity(createDTO)).thenReturn(new DemandeConge());
        when(demandeCongeRepository.save(any())).thenReturn(demande);
        when(demandeCongeMapper.toResponseDTO(any())).thenReturn(new DemandeCongeResponseDTO());

        DemandeCongeResponseDTO result = congeService.soumettreDemande(1L, createDTO, null);

        assertNotNull(result);
        verify(demandeCongeRepository).save(any());
        verify(notificationService, atLeastOnce()).saveAndBroadcastNotification(any());
    }

    @Test
    void soumettreDemande_PastDate_ThrowsException() {
        createDTO.setDateDebut(LocalDate.now().minusDays(1));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> 
            congeService.soumettreDemande(1L, createDTO, null)
        );

        assertEquals("Date de début ne peut pas être dans le passé", exception.getMessage());
    }

    @Test
    void soumettreDemande_InsufficientBalance_ThrowsException() {
        user.setSoldeConges(2.0); // Only 2 days left
        createDTO.setDateDebut(LocalDate.now().plusDays(1));
        createDTO.setDateFin(LocalDate.now().plusDays(5)); // 5 days requested

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(demandeCongeRepository.existsByUserAndStatut(user, StatutDemande.EN_ATTENTE)).thenReturn(false);
        when(demandeCongeRepository.existsByUserAndDateDebutLessThanEqualAndDateFinGreaterThanEqual(any(), any(), any())).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> 
            congeService.soumettreDemande(1L, createDTO, null)
        );

        assertEquals("Solde insuffisant", exception.getMessage());
    }

    @Test
    void traiterDemande_Approuver_Success() {
        ValidationCongeDTO validationDTO = new ValidationCongeDTO();
        validationDTO.setStatut(StatutDemande.VALIDEE);
        validationDTO.setDetails("Approuvé");

        when(demandeCongeRepository.findById(1L)).thenReturn(Optional.of(demande));
        when(demandeCongeRepository.save(any())).thenReturn(demande);
        when(demandeCongeMapper.toResponseDTO(any())).thenReturn(new DemandeCongeResponseDTO());

        DemandeCongeResponseDTO result = congeService.traiterDemande(1L, validationDTO);

        assertNotNull(result);
        assertEquals(17.0, user.getSoldeConges()); // 20 - 3
        verify(userRepository).save(user);
        verify(notificationService).saveAndBroadcastNotification(any());
    }

    @Test
    void traiterDemande_Refuser_Success() {
        ValidationCongeDTO validationDTO = new ValidationCongeDTO();
        validationDTO.setStatut(StatutDemande.REFUSEE);
        validationDTO.setDetails("Refusé");

        when(demandeCongeRepository.findById(1L)).thenReturn(Optional.of(demande));
        when(demandeCongeRepository.save(any())).thenReturn(demande);
        when(demandeCongeMapper.toResponseDTO(any())).thenReturn(new DemandeCongeResponseDTO());

        DemandeCongeResponseDTO result = congeService.traiterDemande(1L, validationDTO);

        assertNotNull(result);
        assertEquals(20.0, user.getSoldeConges()); // No change
        verify(notificationService).saveAndBroadcastNotification(any());
    }
}
