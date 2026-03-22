import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { CongeService } from '../../../core/services/conge.service';
import { AuthService } from '../../../core/services/auth.service';
import { DemandeConge, SoldeConge } from '../../../core/models/conge.model';
import { WebSocketService } from '../../../core/services/websocket.service';
import { Subscription } from 'rxjs';
import { TypeNotification } from '../../../core/models/notification.model';

@Component({
  selector: 'app-demandes-list',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './demandes-list.component.html'
})
export class DemandesListComponent implements OnInit, OnDestroy {
  demandes: DemandeConge[] = [];
  solde: SoldeConge | null = null;
  isLoading = true;
  showCancelModal = false;
  demandeToCancel: DemandeConge | null = null;
  private notificationSubscription?: Subscription;

  constructor(
    private congeService: CongeService,
    private authService: AuthService,
    private webSocketService: WebSocketService
  ) {}

  ngOnInit(): void {
    this.loadData();
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
          if (notif.type === TypeNotification.DEMANDE_VALIDEE || 
              notif.type === TypeNotification.DEMANDE_REFUSEE) {
            this.loadData();
          }
        });
    }
  }

  get userId(): number {
    return this.authService.getCurrentUser()?.id || 0;
  }

  loadData(): void {
    this.isLoading = true;

    this.congeService.getMesDemandes(this.userId).subscribe({
      next: (demandes) => {
        this.demandes = demandes;
        this.isLoading = false;
      },
      error: () => {
        this.isLoading = false;
      }
    });

    this.congeService.getSoldeConges(this.userId).subscribe({
      next: (solde) => {
        this.solde = solde;
      }
    });
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

  confirmCancel(demande: DemandeConge): void {
    this.demandeToCancel = demande;
    this.showCancelModal = true;
  }

  cancelDemande(): void {
    if (this.demandeToCancel) {
      this.congeService.annulerDemande(this.userId, this.demandeToCancel.id).subscribe({
        next: () => {
          this.loadData();
          this.showCancelModal = false;
          this.demandeToCancel = null;
        }
      });
    }
  }

  closeCancelModal(): void {
    this.showCancelModal = false;
    this.demandeToCancel = null;
  }

  downloadFile(pieceJointe: string): void {
    this.congeService.downloadJustificatif(pieceJointe).subscribe({
      next: (blob) => {
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        // Extrait le nom du fichier (après le dernier '/')
        const fileName = pieceJointe.split('/').pop() || 'justificatif';
        a.download = fileName;
        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a);
        window.URL.revokeObjectURL(url);
      },
      error: (err) => {
        console.error('Erreur lors du téléchargement du fichier :', err);
        alert('Impossible de télécharger le fichier.');
      }
    });
  }
}
