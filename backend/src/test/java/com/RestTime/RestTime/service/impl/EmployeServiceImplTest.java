package com.RestTime.RestTime.service.impl;

import com.RestTime.RestTime.dto.SoldeCongeDTO;
import com.RestTime.RestTime.model.entity.DemandeConge;
import com.RestTime.RestTime.model.entity.User;
import com.RestTime.RestTime.model.enumeration.StatutDemande;
import com.RestTime.RestTime.repository.DemandeCongeRepository;
import com.RestTime.RestTime.repository.HistoriqueRepository;
import com.RestTime.RestTime.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private DemandeCongeRepository demandeCongeRepository;
    @Mock
    private HistoriqueRepository historiqueRepository;

    @InjectMocks
    private EmployeServiceImpl employeService;



    private User user;
    private DemandeConge demande;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setSoldeConges(15.0);

        demande = new DemandeConge();
        demande.setId(10L);
        demande.setUser(user);
        demande.setStatut(StatutDemande.EN_ATTENTE);
        demande.setNombreJours(5);

    }

    @Test
    void getSoldeConges_Success() {
        demande.setStatut(StatutDemande.VALIDEE);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(demandeCongeRepository.findByUserOrderByIdDesc(user)).thenReturn(Collections.singletonList(demande));

        SoldeCongeDTO result = employeService.getSoldeConges(1L);

        assertNotNull(result);
        assertEquals(5.0, result.getSoldeUtilise());
        assertEquals(15.0, result.getSoldeRestant());
        assertEquals(20.0, result.getSoldeActuel());
    }

    @Test
    void annulerDemande_Success() {
        when(demandeCongeRepository.findById(10L)).thenReturn(Optional.of(demande));

        employeService.annulerDemande(10L, 1L);

        verify(demandeCongeRepository).delete(demande);
        verify(historiqueRepository).deleteByDemandeConge(demande);
    }

    @Test
    void annulerDemande_Unauthorized_ThrowsException() {
        when(demandeCongeRepository.findById(10L)).thenReturn(Optional.of(demande));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> 
            employeService.annulerDemande(10L, 2L) // Different user ID
        );

        assertEquals("Vous n'êtes pas autorisé à annuler cette demande.", exception.getMessage());
    }


}
