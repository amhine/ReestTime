package com.RestTime.RestTime.controller;

import com.RestTime.RestTime.dto.AnnouncementCreateDTO;
import com.RestTime.RestTime.dto.AnnouncementDTO;
import com.RestTime.RestTime.service.AnnouncementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/announcements")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AnnouncementController {

    private final AnnouncementService announcementService;

    @GetMapping
    public ResponseEntity<List<AnnouncementDTO>> getAll() {
        return ResponseEntity.ok(announcementService.getAllAnnouncements());
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('RH','ROLE_RH')")
    public ResponseEntity<AnnouncementDTO> create(@RequestBody AnnouncementCreateDTO dto) {
        return ResponseEntity.ok(announcementService.createAnnouncement(dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('RH')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        announcementService.deleteAnnouncement(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('RH','ROLE_RH')")
    public ResponseEntity<AnnouncementDTO> update(@PathVariable Long id, @RequestBody AnnouncementCreateDTO dto) {
        return ResponseEntity.ok(announcementService.updateAnnouncement(id, dto));
    }
}
