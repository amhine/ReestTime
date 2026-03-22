export enum TypeNotification {
  DEMANDE_SOUMISE = 'DEMANDE_SOUMISE',
  DEMANDE_VALIDEE = 'DEMANDE_VALIDEE',
  DEMANDE_REFUSEE = 'DEMANDE_REFUSEE',
  ABSENCE_DECLAREE = 'ABSENCE_DECLAREE',
  SOLDE_FAIBLE = 'SOLDE_FAIBLE',
  RAPPEL = 'RAPPEL'
}

export interface NotificationResponse {
  id: number;
  titre: string;
  message: string;
  dateEnvoi: string;
  type: TypeNotification;
  lue: boolean;
  userId: number;
}
