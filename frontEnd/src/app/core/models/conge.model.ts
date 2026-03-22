export interface DemandeConge {
  id: number;
  dateDebut: string;
  dateFin: string;
  nombreJours: number;
  motif: string;
  pieceJointe?: string;
  dateSoumission: string;
  statut: 'EN_ATTENTE' | 'VALIDEE' | 'REFUSEE' | 'ANNULEE';
  type: 'ANNUEL' | 'MALADIE' | 'EXCEPTIONNEL' | 'FORMATION';
  userId: number;
  nom: string;    // <--- Changé (était userNom)
  prenom: string; // <--- Changé (était userPrenom)
  poste?: string; // Selon Swagger, vérifiez si ces champs arrivent aussi
  departement?: string;
}

export interface DemandeCongeCreate {
  dateDebut: string;
  dateFin: string;
  motif: string;
  type: 'ANNUEL' | 'MALADIE' | 'EXCEPTIONNEL' | 'FORMATION';
}

export interface ValidationConge {
  statut: 'VALIDEE' | 'REFUSEE';
  details?: string;
}

export interface SoldeConge {
  soldeActuel: number;
  soldeUtilise: number;
  soldeRestant: number;
}

export interface StatistiquesRh {
  totalDemandes: number;
  demandesApprouvees: number;
  demandesRefusees: number;
  demandesEnAttente: number;
  totalAbsences: number;
  tauxApprobation: number;
}

export interface HistoriqueResponse {
  id: number;
  action: string;
  details: string;
  dateAction: string;
  demandeId: number;
  userId: number;
}
