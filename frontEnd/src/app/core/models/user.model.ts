export interface User {
  id: number;
  nom: string;
  prenom: string;
  email: string;
  role: 'RH' | 'EMPLOYE';
  soldeConges: number;
  telephone?: string;
  adresse?: string;
  photoProfile?: string;
  poste?: string;
  departement?: string;
}

export interface LoginRequest {
  email: string;
  motDePasse: string;
}

export interface AuthResponse {
  token: string;
}

export interface CreateUserRequest {
  nom: string;
  prenom: string;
  email: string;
  motDePasse: string;
  role: 'RH' | 'EMPLOYE';
  soldeConges: number;
  departement: string;
}

export interface UpdateUserRequest {
  nom: string;
  prenom: string;
  email: string;
  soldeConges: number;
  departement: string;
}

export interface UpdateProfileRequest {
  telephone?: string;
  adresse?: string;
  motDePasse?: string;
}
