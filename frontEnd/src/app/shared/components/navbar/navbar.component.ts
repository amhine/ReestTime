import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

export interface NavItem {
  label: string;
  route: string;
  icon?: string;
}

import { NotificationListComponent } from '../notification-list/notification-list.component';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterModule, NotificationListComponent],
  templateUrl: './navbar.component.html'
})
export class NavbarComponent {
  @Input() menuItems: NavItem[] = [];
  @Input() unreadNotifications = 0;
  
  showNotifications = false;
  showUserMenu = false;
  userName: string = '';

  constructor(public authService: AuthService) {
    const user = this.authService.getCurrentUser();
    if (user) {
      this.userName = `${user.prenom} ${user.nom}`;
    }
  }

  logout(): void {
    this.authService.logout();
  }
}
