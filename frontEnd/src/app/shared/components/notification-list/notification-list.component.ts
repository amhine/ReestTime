import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NotificationService } from '../../../core/services/notification.service';
import { AuthService } from '../../../core/services/auth.service';
import { NotificationResponse } from '../../../core/models/notification.model';

@Component({
  selector: 'app-notification-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './notification-list.component.html'
})
export class NotificationListComponent implements OnInit {
  notifications: NotificationResponse[] = [];

  constructor(
    private notificationService: NotificationService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.notificationService.notifications$.subscribe(
      notifs => this.notifications = notifs
    );
  }

  markAsRead(id: number): void {
    const user = this.authService.getCurrentUser();
    if (user) {
      this.notificationService.marquerCommeLue(id, user.id).subscribe();
    }
  }
}
