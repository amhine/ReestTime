package com.RestTime.RestTime.controller;

import com.RestTime.RestTime.dto.NotificationResponseDTO;
import com.RestTime.RestTime.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;


    @GetMapping
    public ResponseEntity<List<NotificationResponseDTO>> getNotificationsNonLues(
            @RequestParam(name = "userId") Long userId) {
        return ResponseEntity.ok(notificationService.getNotificationsNonLues(userId));
    }


    @PutMapping("/{id}/read")
    public ResponseEntity<NotificationResponseDTO> marquerCommeLue(
            @PathVariable(name = "id") Long id,
            @RequestParam(name = "userId") Long userId) {
        return ResponseEntity.ok(notificationService.marquerCommeLue(id, userId));
    }
}