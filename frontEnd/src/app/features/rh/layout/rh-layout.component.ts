import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { SidebarComponent } from '../../../shared/components/sidebar/sidebar.component';
import { NotificationListComponent } from '../../../shared/components/notification-list/notification-list.component';
import { NotificationService } from '../../../core/services/notification.service';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-rh-layout',
  standalone: true,
  imports: [CommonModule, RouterModule, SidebarComponent, NotificationListComponent],
  templateUrl: './rh-layout.component.html'
})
export class RhLayoutComponent implements OnInit {
  showNotifications = false;
  unreadCount = 0;
  menuItems = [
    { label: 'Tableau de bord', route: '/rh/dashboard', icon: 'dashboard' },
    { label: 'Employés', route: '/rh/employes', icon: 'users' },
    { label: 'Annonces', route: '/rh/announcements', icon: 'megaphone' },
    { label: 'Demandes de congés', route: '/rh/demandes', icon: 'document' }
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
