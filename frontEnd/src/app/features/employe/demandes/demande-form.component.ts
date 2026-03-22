import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterModule, ActivatedRoute } from '@angular/router';
import { CongeService } from '../../../core/services/conge.service';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-demande-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './demande-form.component.html'
})
export class DemandeFormComponent implements OnInit {
  form: FormGroup;
  isSaving = false;
  isEditMode = false;
  demandeId: number | null = null;
  errorMessage = '';
  selectedFile: File | null = null;
  selectedFileName = '';

  typesConge = [
    { value: 'ANNUEL', label: 'Congé annuel' },
    { value: 'MALADIE', label: 'Congé maladie (Certificat requis)' },
    { value: 'EXCEPTIONNEL', label: 'Congé exceptionnel' },
    { value: 'FORMATION', label: 'Formation' }
  ];

  constructor(
    private fb: FormBuilder,
    private congeService: CongeService,
    private authService: AuthService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.form = this.fb.group({
      type: ['ANNUEL', Validators.required],
      dateDebut: ['', Validators.required],
      dateFin: ['', Validators.required],
      motif: ['']
    });
  }

  get userId(): number {
    return this.authService.getCurrentUser()?.id || 0;
  }

  onFileSelected(event: any): void {
    const file = event.target.files[0];
    if (file) {
      if (file.size > 5 * 1024 * 1024) { // 5MB max
        this.errorMessage = "Le fichier ne doit pas dépasser 5 MB.";
        return;
      }
      this.selectedFile = file;
      this.selectedFileName = file.name;
      this.errorMessage = '';
    }
  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      if (params['id']) {
        this.isEditMode = true;
        this.demandeId = +params['id'];
        this.loadDemande(this.demandeId);
      }
    });
  }

  loadDemande(id: number): void {
    this.congeService.getMesDemandes(this.userId).subscribe(demandes => {
      const demande = demandes.find(d => d.id === id);
      if (demande) {
        if (demande.statut !== 'EN_ATTENTE') {
          this.router.navigate(['/employe/demandes']);
          return;
        }
        this.form.patchValue({
          type: demande.type,
          dateDebut: demande.dateDebut,
          dateFin: demande.dateFin,
          motif: demande.motif
        });
      }
    });
  }

  onSubmit(): void {
    if (this.form.invalid) return;
    
    // Check if maladie but no file
    if (this.form.value.type === 'MALADIE' && !this.selectedFile) {
      this.errorMessage = "Un certificat médical (fichier) est requis pour un congé maladie.";
      return;
    }

    this.isSaving = true;
    this.errorMessage = '';

    const obs = this.isEditMode && this.demandeId 
      ? this.congeService.modifierDemande(this.userId, this.demandeId, this.form.value, this.selectedFile || undefined)
      : this.congeService.soumettreDemande(this.userId, this.form.value, this.selectedFile || undefined);

    obs.subscribe({
      next: () => {
        this.router.navigate(['/employe/demandes']);
      },
      error: (err) => {
        this.isSaving = false;
        this.errorMessage = err.error?.message || 'Erreur lors de l\'enregistrement';
      }
    });
  }
}
