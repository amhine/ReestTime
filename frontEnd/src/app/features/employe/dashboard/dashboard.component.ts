import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CongeService } from '../../../core/services/conge.service';
import { AuthService } from '../../../core/services/auth.service';
import { AnnouncementService } from '../../../core/services/announcement.service';
import { AttendanceService } from '../../../core/services/attendance.service';
import { SoldeConge, DemandeConge } from '../../../core/models/conge.model';
import { Announcement } from '../../../core/models/announcement.model';
import { Attendance } from '../../../core/models/attendance.model';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-employe-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './dashboard.component.html'
})
export class DashboardComponent implements OnInit {
  solde?: SoldeConge;
  demandes: DemandeConge[] = [];
  announcements: Announcement[] = [];
  todayAttendance?: Attendance;
  userName: string = '';
  today: Date = new Date();

  constructor(
    private congeService: CongeService,
    public authService: AuthService,
    private announcementService: AnnouncementService,
    private attendanceService: AttendanceService
  ) { }

  ngOnInit(): void {
    const user = this.authService.getCurrentUser();
    if (user) {
      this.userName = `${user.prenom} ${user.nom}`;
      this.congeService.getSoldeConges(user.id).subscribe(res => this.solde = res);
      this.congeService.getMesDemandes(user.id).subscribe(res => this.demandes = res);
      this.announcementService.getAllAnnouncements().subscribe(res => this.announcements = res);
      this.attendanceService.getTodayAttendance(user.id).subscribe({
        next: (res) => this.todayAttendance = res,
        error: () => console.log('No attendance record for today yet.')
      });
    }
  }

  getPendingRequestsCount(): number {
    return this.demandes.filter(d => d.statut === 'EN_ATTENTE').length;
  }

  getStatutClass(statut: string): string {
    switch (statut) {
      case 'VALIDEE': return 'bg-emerald-500';
      case 'REFUSEE': return 'bg-rose-500';
      case 'EN_ATTENTE': return 'bg-amber-500';
      default: return 'bg-gray-300';
    }
  }

  getStatutBadgeClass(statut: string): string {
    switch (statut) {
      case 'VALIDEE': return 'bg-emerald-100 text-emerald-700 border border-emerald-200';
      case 'REFUSEE': return 'bg-rose-100 text-rose-700 border border-rose-200';
      case 'EN_ATTENTE': return 'bg-amber-100 text-amber-700 border border-amber-200';
      default: return 'bg-gray-100 text-gray-600 border border-gray-200';
    }
  }
}
