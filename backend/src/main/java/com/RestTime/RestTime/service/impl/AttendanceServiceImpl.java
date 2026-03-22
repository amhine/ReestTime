package com.RestTime.RestTime.service.impl;

import com.RestTime.RestTime.dto.AttendanceDTO;
import com.RestTime.RestTime.model.entity.Attendance;
import com.RestTime.RestTime.model.entity.User;
import com.RestTime.RestTime.repository.AttendanceRepository;
import com.RestTime.RestTime.repository.UserRepository;
import com.RestTime.RestTime.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;

    private static final LocalTime WORK_START_TIME = LocalTime.of(9, 0);

    @Override
    public AttendanceDTO clockIn(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        if (attendanceRepository.findByUserAndDate(user, LocalDate.now()).isPresent()) {
            throw new RuntimeException("Vous avez déjà pointé votre entrée aujourd'hui.");
        }

        LocalTime maintenant = LocalTime.now();
        if (maintenant.isBefore(WORK_START_TIME)) {
            throw new RuntimeException("Le pointage n'est pas autorisé avant 09:00.");
        }
        Attendance attendance = new Attendance();
        attendance.setUser(user);
        attendance.setDate(LocalDate.now());
        attendance.setHeureEntree(maintenant);

        if (maintenant.isAfter(WORK_START_TIME)) {
            long minutes = ChronoUnit.MINUTES.between(WORK_START_TIME, maintenant);
            attendance.setMinutesRetard(minutes);
            attendance.setEstEnRetard(true);
        } else {
            attendance.setMinutesRetard(0L);
            attendance.setEstEnRetard(false);
        }

        return convertToDTO(attendanceRepository.save(attendance));
    }

    @Override
    public AttendanceDTO clockOut(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        Attendance attendance = attendanceRepository.findByUserAndDate(user, LocalDate.now())
                .orElseThrow(() -> new RuntimeException("Pas de pointage d'entrée pour aujourd'hui"));

        if (attendance.getHeureSortie() != null) {
            throw new RuntimeException("Déjà pointé la sortie");
        }

        LocalTime now = LocalTime.now();
        attendance.setHeureSortie(now);

        Duration duration = Duration.between(attendance.getHeureEntree(), now);
        double hours = duration.toMinutes() / 60.0;
        attendance.setHeuresTravaillees(Math.round(hours * 100.0) / 100.0);

        return convertToDTO(attendanceRepository.save(attendance));
    }

    @Override
    public List<AttendanceDTO> getMyAttendanceHistory(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
        return attendanceRepository.findByUserIdAndDateOrderByDateDesc(userId, LocalDate.now())
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AttendanceDTO> getTodayAttendance() {
        return attendanceRepository.findByDate(LocalDate.now())
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AttendanceDTO getTodayAttendance(Long userId) {
        return attendanceRepository
                .findByUserIdAndDateOrderByDateDesc(userId, LocalDate.now())
                .map(this::convertToDTO)
                .orElse(null);
    }

    private AttendanceDTO convertToDTO(Attendance entity) {
        return AttendanceDTO.builder()
                .id(entity.getId())
                .date(entity.getDate())
                .heureEntree(entity.getHeureEntree())
                .heureSortie(entity.getHeureSortie())
                .heuresTravaillees(entity.getHeuresTravaillees())
                .estEnRetard(entity.getEstEnRetard())
                .minutesRetard(entity.getMinutesRetard())
                .userId(entity.getUser().getId())
                .prenom(entity.getUser().getPrenom())
                .nom(entity.getUser().getNom())
                .build();
    }
}
