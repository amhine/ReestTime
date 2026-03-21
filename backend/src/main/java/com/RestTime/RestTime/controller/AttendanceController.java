package com.RestTime.RestTime.controller;

import com.RestTime.RestTime.dto.AttendanceDTO;
import com.RestTime.RestTime.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping("/clock-in/{userId}")
    public ResponseEntity<?> clockIn(@PathVariable Long userId) {
        try {
            return ResponseEntity.ok(attendanceService.clockIn(userId));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/clock-out/{userId}")
    public ResponseEntity<AttendanceDTO> clockOut(@PathVariable Long userId) {
        return ResponseEntity.ok(attendanceService.clockOut(userId));
    }

    @GetMapping("/history/{userId}")
    public ResponseEntity<List<AttendanceDTO>> getHistory(@PathVariable Long userId) {
        return ResponseEntity.ok(attendanceService.getMyAttendanceHistory(userId));
    }

    @GetMapping("/today")
    @PreAuthorize("hasRole('RH')")
    public ResponseEntity<List<AttendanceDTO>> getToday() {
        return ResponseEntity.ok(attendanceService.getTodayAttendance());
    }

    @GetMapping("/today/user/{userId}")
    @PreAuthorize("hasRole('EMPLOYE') or hasRole('RH')")
    public ResponseEntity<AttendanceDTO> getTodayForUser(@PathVariable Long userId) {
        return ResponseEntity.ok(attendanceService.getTodayAttendance(userId));
    }
}
