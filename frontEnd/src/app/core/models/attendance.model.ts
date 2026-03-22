import { User } from './user.model';

export interface Attendance {
  id: number;
  date: string;
  heureEntree: string;
  heureSortie: string | null;
  heuresTravaillees: number;
  estEnRetard: boolean;
  minutesRetard: number;
  user?: User;
  userId?: number;
  prenom?: string;
  nom?: string;
}
