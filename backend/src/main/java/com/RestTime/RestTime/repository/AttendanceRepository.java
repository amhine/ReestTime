package com.RestTime.RestTime.repository;

import com.RestTime.RestTime.model.entity.Attendance;
import com.RestTime.RestTime.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    Optional<Attendance> findByUserAndDate(User user, LocalDate date);

    Optional<Attendance> findByUserIdAndDateOrderByDateDesc(Long userId, LocalDate date);

    List<Attendance> findByDate(LocalDate date);
    long countByDate(LocalDate date);
}
