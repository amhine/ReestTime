import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Attendance } from '../models/attendance.model';

@Injectable({
  providedIn: 'root'
})
export class AttendanceService {
  private apiUrl = `${environment.apiUrl}/attendance`;

  constructor(private http: HttpClient) {}

  clockIn(userId: number): Observable<Attendance> {
    return this.http.post<Attendance>(`${this.apiUrl}/clock-in/${userId}`, {});
  }

  clockOut(userId: number): Observable<Attendance> {
    return this.http.post<Attendance>(`${this.apiUrl}/clock-out/${userId}`, {});
  }

  getTodayAttendance(userId: number): Observable<Attendance> {
    return this.http.get<Attendance>(`${this.apiUrl}/today/user/${userId}`);
  }

  getAllTodayAttendances(): Observable<Attendance[]> {
    return this.http.get<Attendance[]>(`${this.apiUrl}/today`);
  }

  getHistory(userId: number): Observable<Attendance[]> {
    return this.http.get<Attendance[]>(`${this.apiUrl}/history/${userId}`);
  }
}
