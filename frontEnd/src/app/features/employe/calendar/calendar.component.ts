import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CongeService } from '../../../core/services/conge.service';
import { AuthService } from '../../../core/services/auth.service';
import { forkJoin } from 'rxjs';

export interface CalendarEvent {
  date: string;
  title: string;
  type: 'LEAVE' | 'HOLIDAY';
  duration?: number;
}

@Component({
  selector: 'app-calendar',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './calendar.component.html'
})
export class CalendarComponent implements OnInit {
  events: CalendarEvent[] = [];
  searchTitle: string = '';
  isLoading = true;

  constructor(
    private congeService: CongeService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    const userId = this.authService.getCurrentUser()?.id;
    if (!userId) {
      this.isLoading = false;
      return;
    }

    this.isLoading = true;

    forkJoin({
      demandes: this.congeService.getMesDemandes(userId),
      holidays25: this.congeService.getPublicHolidays(2025),
      holidays26: this.congeService.getPublicHolidays(2026)
    }).subscribe({
      next: (res) => {
        const leaveEvents: CalendarEvent[] = res.demandes
          .filter(d => d.statut === 'VALIDEE')
          .map(d => ({
            date: d.dateDebut,
            title: `Congé: ${d.type}`,
            type: 'LEAVE' as const,
            duration: d.nombreJours
          }));

        const holidayEvents: CalendarEvent[] = [...res.holidays25, ...res.holidays26];

        this.events = [...leaveEvents, ...holidayEvents]
          .sort((a, b) => new Date(b.date).getTime() - new Date(a.date).getTime());

        this.isLoading = false;
      },
      error: () => this.isLoading = false
    });
  }

  isUpcoming(date: string): boolean {
    return new Date(date) >= new Date();
  }

  getFilteredEvents(): CalendarEvent[] {
    if (!this.searchTitle || this.searchTitle.trim() === '') {
      return this.events;
    }
    const search = this.searchTitle.toLowerCase().trim();
    return this.events.filter(event => 
      event.title.toLowerCase().includes(search)
    );
  }
}
