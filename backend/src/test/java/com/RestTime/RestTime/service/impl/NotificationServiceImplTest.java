package com.RestTime.RestTime.service.impl;

import com.RestTime.RestTime.dto.NotificationResponseDTO;
import com.RestTime.RestTime.mapper.NotificationMapper;
import com.RestTime.RestTime.model.entity.Notification;
import com.RestTime.RestTime.model.entity.User;
import com.RestTime.RestTime.repository.NotificationRepository;
import com.RestTime.RestTime.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceImplTest {

    @Mock
    private NotificationRepository notificationRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private NotificationMapper notificationMapper;
    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    private User user;
    private Notification notification;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);

        notification = new Notification();
        notification.setId(100L);
        notification.setUser(user);
        notification.setLue(false);
    }

    @Test
    void getNotificationsNonLues_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(notificationRepository.findByUserAndLueFalse(user)).thenReturn(Collections.singletonList(notification));
        when(notificationMapper.toResponseDTO(notification)).thenReturn(new NotificationResponseDTO());

        var result = notificationService.getNotificationsNonLues(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void marquerCommeLue_Success() {
        when(notificationRepository.findById(100L)).thenReturn(Optional.of(notification));
        when(notificationMapper.toResponseDTO(notification)).thenReturn(new NotificationResponseDTO());

        notificationService.marquerCommeLue(100L, 1L);

        assertTrue(notification.isLue());
        verify(notificationRepository).save(notification);
    }

    @Test
    void saveAndBroadcastNotification_Success() {
        when(notificationMapper.toResponseDTO(notification)).thenReturn(new NotificationResponseDTO());

        notificationService.saveAndBroadcastNotification(notification);

        verify(notificationRepository).save(notification);
        verify(messagingTemplate).convertAndSend(eq("/topic/notifications/1"), any(NotificationResponseDTO.class));
    }
}
