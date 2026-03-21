package com.RestTime.RestTime.service;

import com.RestTime.RestTime.dto.NotificationResponseDTO;
import java.util.List;

public interface NotificationService {

    List<NotificationResponseDTO> getNotificationsNonLues(Long userId);

    NotificationResponseDTO marquerCommeLue(Long notificationId, Long userId);

    void saveAndBroadcastNotification(com.RestTime.RestTime.model.entity.Notification notification);
}