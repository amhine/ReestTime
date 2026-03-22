import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { tap } from 'rxjs/operators';
import { environment } from '../../../environments/environment';
import { NotificationResponse } from '../models/notification.model';
import { WebSocketService } from './websocket.service';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  private apiUrl = environment.apiUrl;

  private notificationsSubject = new BehaviorSubject<NotificationResponse[]>([]);
  public notifications$ = this.notificationsSubject.asObservable();

  private unreadCountSubject = new BehaviorSubject<number>(0);
  public unreadCount$ = this.unreadCountSubject.asObservable();

  private initializedUserId: number | null = null;
  private wsSubscription: any;

  constructor(
    private http: HttpClient,
    private webSocketService: WebSocketService
  ) { }

  init(userId: number): void {
    if (this.initializedUserId == userId && this.wsSubscription) return;

    if (this.wsSubscription) {
      this.wsSubscription.unsubscribe();
    }

    this.initializedUserId = userId;
    this.getNotificationsNonLues(userId).subscribe();

    this.wsSubscription = this.webSocketService.watchUserNotifications(userId).subscribe(
      newNotif => {
        const currentNotifs = this.notificationsSubject.value;
        const exists = currentNotifs.some(n => n.id === newNotif.id);

        if (!exists) {
          this.notificationsSubject.next([newNotif, ...currentNotifs]);
          this.unreadCountSubject.next(this.unreadCountSubject.value + 1);

          console.log('Notification reçue et ajoutée à la liste.');
        }
      },
      err => console.error('Erreur WebSocket NotificationService:', err)
    );
  }

  getNotificationsNonLues(userId: number): Observable<NotificationResponse[]> {
    return this.http.get<NotificationResponse[]>(`${this.apiUrl}/notifications`, {
      params: { userId: userId.toString() }
    }).pipe(
      tap(notifications => {
        this.notificationsSubject.next(notifications);
        this.unreadCountSubject.next(notifications.length);
      })
    );
  }

  marquerCommeLue(id: number, userId: number): Observable<NotificationResponse> {
    return this.http.put<NotificationResponse>(`${this.apiUrl}/notifications/${id}/read`, null, {
      params: { userId: userId.toString() }
    }).pipe(
      tap(() => {
        const currentNotifs = this.notificationsSubject.value.filter(n => n.id !== id);
        this.notificationsSubject.next(currentNotifs);
        this.unreadCountSubject.next(Math.max(0, this.unreadCountSubject.value - 1));
      })
    );
  }
}
