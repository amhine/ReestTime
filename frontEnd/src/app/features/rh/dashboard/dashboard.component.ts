import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CongeService } from '../../../core/services/conge.service';
import { AttendanceService } from '../../../core/services/attendance.service';
import { StatistiquesRh, HistoriqueResponse, DemandeConge } from '../../../core/models/conge.model';
import { Attendance } from '../../../core/models/attendance.model';

@Component({
  selector: 'app-rh-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './dashboard.component.html'
})
export class DashboardComponent implements OnInit {
  stats?: StatistiquesRh;
  
  activeTab: 'conges' | 'attendances' = 'attendances';
  historiqueActions: HistoriqueResponse[] = [];
  allDemandes: DemandeConge[] = [];
  todayAttendances: Attendance[] = [];
  filterStatus = 'ALL';
  isLoadingHistory = true;

  constructor(
    private congeService: CongeService,
    private attendanceService: AttendanceService
  ) {}

  ngOnInit(): void {
    this.congeService.getStatistiquesRh().subscribe(res => this.stats = res);
    this.loadHistory();
  }

  loadHistory(): void {
    this.isLoadingHistory = true;
    
    this.congeService.getHistoriqueGlobal().subscribe({
      next: (res) => this.historiqueActions = res,
      error: (err) => console.error('Erreur historique actions', err)
    });

    this.congeService.getAllDemandes().subscribe({
      next: (res) => this.allDemandes = res,
      error: (err) => console.error('Erreur liste demandes', err)
    });

    this.attendanceService.getAllTodayAttendances().subscribe({
      next: (res) => {
        this.todayAttendances = res;
        this.isLoadingHistory = false;
      },
      error: (err) => {
        console.error('Erreur pointages du jour', err);
        this.isLoadingHistory = false;
      }
    });
  }

  getFilteredAttendances(): Attendance[] {
    if (this.filterStatus === 'LATE') {
      return this.todayAttendances.filter(a => a.estEnRetard);
    }
    if (this.filterStatus === 'ON_TIME') {
      return this.todayAttendances.filter(a => !a.estEnRetard);
    }
    return this.todayAttendances;
  }

  getStatutClass(statut: string): string {
    const classes: Record<string, string> = {
      'EN_ATTENTE': 'bg-yellow-100 text-yellow-700',
      'VALIDEE': 'bg-green-100 text-green-700',
      'REFUSEE': 'bg-red-100 text-red-700',
      'ANNULEE': 'bg-gray-100 text-gray-700'
    };
    return classes[statut] || 'bg-gray-100 text-gray-700';
  }

  getStatutLabel(statut: string): string {
    const labels: Record<string, string> = {
      'EN_ATTENTE': 'En attente',
      'VALIDEE': 'Validée',
      'REFUSEE': 'Refusée',
      'ANNULEE': 'Annulée'
    };
    return labels[statut] || statut;
  }

  getTypeLabel(type: string): string {
    const labels: Record<string, string> = {
      'ANNUEL': 'Congé annuel',
      'MALADIE': 'Congé maladie',
      'EXCEPTIONNEL': 'Congé exceptionnel',
      'FORMATION': 'Formation'
    };
    return labels[type] || type;
  }

  downloadJustificatif(fileName: string): void {
    if (!fileName) return;
    this.congeService.downloadJustificatif(fileName).subscribe({
      next: (blob) => {
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = fileName.split('/').pop() || 'justificatif';
        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a);
        window.URL.revokeObjectURL(url);
      },
      error: (err) => {
        console.error('Erreur téléchargement justificatif :', err);
      }
    });
  }
}
