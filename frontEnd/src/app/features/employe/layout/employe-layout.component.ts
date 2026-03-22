import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { NavbarComponent } from '../../../shared/components/navbar/navbar.component';
import { NotificationService } from '../../../core/services/notification.service';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-employe-layout',
  standalone: true,
  imports: [CommonModule, RouterModule, NavbarComponent],
  templateUrl: './employe-layout.component.html'
})
export class EmployeLayoutComponent implements OnInit {
  showNotifications = false;
  unreadCount = 0;
  menuItems = [
    { label: 'Tableau de bord', route: '/employe/dashboard', icon: 'dashboard' },
    { label: 'Pointage', route: '/employe/attendance', icon: 'clock' },
    { label: 'Mes demandes', route: '/employe/demandes', icon: 'document' },
    { label: 'Calendrier', route: '/employe/calendar', icon: 'calendar' },
    { label: 'Mon profil', route: '/employe/profil', icon: 'user' }
  ];

  constructor(
    private notificationService: NotificationService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    const user = this.authService.getCurrentUser();
    if (user) {
      this.notificationService.init(user.id);
      
      this.notificationService.unreadCount$.subscribe(
        count => this.unreadCount = count
      );
    }
  }
}
