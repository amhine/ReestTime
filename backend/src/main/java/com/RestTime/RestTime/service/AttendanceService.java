package com.RestTime.RestTime.service;

import com.RestTime.RestTime.dto.AttendanceDTO;
import com.RestTime.RestTime.model.entity.Attendance;
import com.RestTime.RestTime.model.entity.User;
import com.RestTime.RestTime.repository.AttendanceRepository;
import com.RestTime.RestTime.repository.UserRepository;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;


public interface AttendanceService {

    AttendanceDTO clockIn(Long userId) ;

     AttendanceDTO clockOut(Long userId);

     List<AttendanceDTO> getMyAttendanceHistory(Long userId) ;

     List<AttendanceDTO> getTodayAttendance() ;

     AttendanceDTO getTodayAttendance(Long userId) ;

}
