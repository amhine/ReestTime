import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AttendanceService } from '../../../core/services/attendance.service';
import { AuthService } from '../../../core/services/auth.service';
import { Attendance } from '../../../core/models/attendance.model';

@Component({
  selector: 'app-attendance',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './attendance.component.html'
})
export class AttendanceComponent implements OnInit {
  todayAttendance?: Attendance;
  history: Attendance[] = [];
  isLoading = false;
  successMessage = '';
  errorMessage = '';

  constructor(
    private attendanceService: AttendanceService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {
    const user = this.authService.getCurrentUser();
    if (!user) return;

    this.attendanceService.getTodayAttendance(user.id).subscribe({
      next: (res: any) => this.todayAttendance = res,
      error: () => this.todayAttendance = undefined
    });

    this.attendanceService.getHistory(user.id).subscribe({
      next: (res: any) => this.history = res,
      error: (err: any) => console.error('Error loading history', err)
    });
  }

  clockIn(): void {
    const now = new Date();
    const currentHour = now.getHours();
    const currentMinute = now.getMinutes();

    // Validation côté client (Optionnel mais recommandé)
    if (currentHour < 9) {
      this.errorMessage = "Il est trop tôt. Le pointage commence à 09:00.";
      return;
    }

    const user = this.authService.getCurrentUser();
    if (!user) return;

    this.isLoading = true;
    this.attendanceService.clockIn(user.id).subscribe({
      next: (res: Attendance) => {
        this.todayAttendance = res;
        this.isLoading = false;

        // Calculer le message de retard pour l'affichage immédiat
        if (res.minutesRetard > 0) {
          this.successMessage = `Pointage réussi. Retard de ${res.minutesRetard} minutes.`;
        } else {
          this.successMessage = "Pointage d'entrée enregistré à l'heure.";
        }
        this.loadData();
      },
      error: (err: any) => {
        this.isLoading = false;
        this.errorMessage = err.error?.message || 'Erreur lors du pointage.';
      }
    });
  }
  clockOut(): void {
    const user = this.authService.getCurrentUser();
    if (!user) return;

    this.isLoading = true;
    this.errorMessage = '';
    this.successMessage = '';

    this.attendanceService.clockOut(user.id).subscribe({
      next: (res: any) => {
        this.todayAttendance = res;
        this.isLoading = false;
        this.successMessage = 'Pointage de sortie enregistré avec succès à ' + res.heureSortie + '. Heures travaillées: ' + res.heuresTravaillees + 'h';
        this.loadData();
      },
      error: (err: any) => {
        this.isLoading = false;
        this.errorMessage = err.error?.message || 'Erreur lors du pointage de sortie.';
      }
    });
  }
}
