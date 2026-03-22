import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { DemandeConge, DemandeCongeCreate, SoldeConge, StatistiquesRh, HistoriqueResponse } from '../models/conge.model';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class CongeService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  // Employé endpoints
  getMesDemandes(userId: number): Observable<DemandeConge[]> {
    return this.http.get<DemandeConge[]>(`${this.apiUrl}/employes/${userId}/mesdemandes`);
  }

  soumettreDemande(userId: number, demande: DemandeCongeCreate, file?: File): Observable<DemandeConge> {
    const formData = new FormData();
    const dtoBlob = new Blob([JSON.stringify(demande)], { type: 'application/json' });
    formData.append('dto', dtoBlob);
    if (file) {
      formData.append('file', file);
    }
    return this.http.post<DemandeConge>(`${this.apiUrl}/employes/conges/${userId}`, formData);
  }

  modifierDemande(userId: number, demandeId: number, demande: DemandeCongeCreate, file?: File): Observable<DemandeConge> {
    const formData = new FormData();
    const dtoBlob = new Blob([JSON.stringify(demande)], { type: 'application/json' });
    formData.append('dto', dtoBlob);
    if (file) {
      formData.append('file', file);
    }
    return this.http.put<DemandeConge>(`${this.apiUrl}/employes/conges/${userId}/${demandeId}`, formData);
  }

  annulerDemande(userId: number, demandeId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/employes/${userId}/demandes/${demandeId}`);
  }

  getSoldeConges(userId: number): Observable<SoldeConge> {
    return this.http.get<SoldeConge>(`${this.apiUrl}/employes/${userId}/solde`);
  }

  // RH endpoints
  getStatistiquesRh(): Observable<StatistiquesRh> {
    return this.http.get<StatistiquesRh>(`${this.apiUrl}/rh/conges/statistiques`);
  }

  getHistoriqueGlobal(): Observable<HistoriqueResponse[]> {
    return this.http.get<HistoriqueResponse[]>(`${this.apiUrl}/rh/conges/historique`);
  }

  getDemandesEnAttente(): Observable<DemandeConge[]> {
    return this.http.get<DemandeConge[]>(`${this.apiUrl}/rh/conges/enattente`);
  }

  getAllDemandes(): Observable<DemandeConge[]> {
    return this.http.get<DemandeConge[]>(`${this.apiUrl}/rh/conges/all`);
  }

  traiterDemande(demandeId: number, decision: { statut: string; details: string }): Observable<DemandeConge> {
    return this.http.put<DemandeConge>(`${this.apiUrl}/rh/conges/${demandeId}/traiter`, decision);
  }

  downloadJustificatif(pieceJointe: string): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/files/${pieceJointe}`, { responseType: 'blob' });
  }

  getPublicHolidays(year: number): Observable<any[]> {
    const apiKey = 'SbVFag1APOnXT4ncvBtC8gGemTtYX3H2';
    return this.http.get<any>(
      `https://calendarific.com/api/v2/holidays?&api_key=${apiKey}&country=MA&year=${year}`
    ).pipe(
      map((response: any) => response.response.holidays.map((h: any) => ({
        date: h.date.iso,
        title: h.name,
        type: 'HOLIDAY' as const
      })))
    );
  }
}

export interface PublicHoliday {
  date: string;
  localName: string;
  name: string;
}
