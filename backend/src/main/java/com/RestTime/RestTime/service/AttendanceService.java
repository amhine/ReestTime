package com.RestTime.RestTime.service;

import com.RestTime.RestTime.dto.AttendanceDTO;
import java.util.List;


public interface AttendanceService {

    AttendanceDTO clockIn(Long userId) ;

     AttendanceDTO clockOut(Long userId);

     List<AttendanceDTO> getMyAttendanceHistory(Long userId) ;

     List<AttendanceDTO> getTodayAttendance() ;

     AttendanceDTO getTodayAttendance(Long userId) ;

}
