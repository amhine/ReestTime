package com.RestTime.RestTime.service.impl;

import com.RestTime.RestTime.dto.AttendanceDTO;
import com.RestTime.RestTime.model.entity.Attendance;
import com.RestTime.RestTime.model.entity.User;
import com.RestTime.RestTime.repository.AttendanceRepository;
import com.RestTime.RestTime.repository.UserRepository;
import com.RestTime.RestTime.service.AttendanceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AttendanceServiceTest {

    @Mock
    private AttendanceRepository attendanceRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AttendanceService attendanceService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
    }

    @Test
    void testClockIn_OnTime() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(attendanceRepository.findByUserAndDate(any(User.class), any(LocalDate.class))).thenReturn(Optional.empty());
        
        when(attendanceRepository.save(any(Attendance.class))).thenAnswer(invocation -> {
            Attendance a = invocation.getArgument(0);
            a.setId(10L);
            return a;
        });

        AttendanceDTO dto = attendanceService.clockIn(1L);

        assertNotNull(dto);
        assertEquals(10L, dto.getId());
        assertEquals(1L, dto.getUserId());
        assertNotNull(dto.getHeureEntree());
    }

    @Test
    void testClockOut_CalculatesHours() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        
        Attendance attendance = new Attendance();
        attendance.setUser(testUser);
        attendance.setId(10L);
        attendance.setDate(LocalDate.now());
        attendance.setHeureEntree(LocalTime.now().minusHours(8));
        
        when(attendanceRepository.findByUserAndDate(any(User.class), any(LocalDate.class)))
            .thenReturn(Optional.of(attendance));

        when(attendanceRepository.save(any(Attendance.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AttendanceDTO dto = attendanceService.clockOut(1L);

        assertNotNull(dto.getHeureSortie());
        assertTrue(dto.getHeuresTravaillees() >= 7.9);
    }

    @Test
    void testClockIn_AlreadyClockedIn_ThrowsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(attendanceRepository.findByUserAndDate(any(User.class), any(LocalDate.class)))
            .thenReturn(Optional.of(new Attendance()));

        assertThrows(RuntimeException.class, () -> attendanceService.clockIn(1L));
    }
}
