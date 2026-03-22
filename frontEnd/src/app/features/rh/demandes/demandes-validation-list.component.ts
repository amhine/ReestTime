import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CongeService } from '../../../core/services/conge.service';
import { DemandeConge } from '../../../core/models/conge.model';
import { FormsModule } from '@angular/forms';
import { WebSocketService } from '../../../core/services/websocket.service';
import { AuthService } from '../../../core/services/auth.service';
import { Subscription } from 'rxjs';
import { TypeNotification } from '../../../core/models/notification.model';

@Component({
  selector: 'app-demandes-validation-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './demandes-validation-list.component.html'
})
export class DemandesValidationListComponent implements OnInit, OnDestroy {
  demandes: DemandeConge[] = [];
  fileBaseUrl = 'http://localhost:8083/api/files/'; // Déclaration correcte
  private notificationSubscription?: Subscription;

  constructor(
    private congeService: CongeService,
    private webSocketService: WebSocketService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.loadDemandes();
    this.setupWebSocket();
  }

  ngOnDestroy(): void {
    if (this.notificationSubscription) {
      this.notificationSubscription.unsubscribe();
    }
  }

  setupWebSocket(): void {
    const user = this.authService.getCurrentUser();
    if (user) {
      this.notificationSubscription = this.webSocketService
        .watchUserNotifications(user.id)
        .subscribe((notif) => {
          if (notif.type === TypeNotification.DEMANDE_SOUMISE) {
            this.loadDemandes();
          }
        });
    }
  }

  loadDemandes(): void {
    this.congeService.getDemandesEnAttente().subscribe(res => this.demandes = res);
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
        alert('Impossible de télécharger le fichier.');
      }
    });
  }

  valider(id: number, statut: 'VALIDEE' | 'REFUSEE'): void {
    const details = prompt('Commentaire (optionnel) :') || '';
    this.congeService.traiterDemande(id, { statut, details }).subscribe(() => {
      this.loadDemandes();
    });
  }
}
