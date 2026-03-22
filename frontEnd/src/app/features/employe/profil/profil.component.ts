import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../../core/services/auth.service';
import { UserService } from '../../../core/services/user.service';
import { User } from '../../../core/models/user.model';

@Component({
  selector: 'app-profil',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './profil.component.html'
})
export class ProfilComponent implements OnInit {
  user: User | null = null;
  profileForm!: FormGroup;
  selectedFile: File | null = null;
  selectedFileName: string = '';
  photoPreview: string | null = null;
  isSubmitting = false;
  successMessage = '';
  errorMessage = '';

  constructor(
    private authService: AuthService,
    private userService: UserService,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.user = this.authService.getCurrentUser();
    this.initForm();
  }

  initForm(): void {
    this.profileForm = this.fb.group({
      telephone: [this.user?.telephone || '', Validators.pattern('^[0-9+ ]{8,15}$')],
      adresse: [this.user?.adresse || ''],
      motDePasse: ['']
    });

    if (this.user?.photoProfile) {
      this.photoPreview = 'http://localhost:8083/api/files/' + this.user.photoProfile;
    }
  }

  onSubmit(): void {
    if (this.profileForm.invalid || !this.user) {
      return;
    }

    this.isSubmitting = true;
    this.successMessage = '';
    this.errorMessage = '';

    const updateData = {
      telephone: this.profileForm.value.telephone,
      adresse: this.profileForm.value.adresse,
      motDePasse: this.profileForm.value.motDePasse
    };

    if (!updateData.motDePasse) {
      delete updateData.motDePasse;
    }

    this.userService.updateProfile(this.user.id, updateData).subscribe({
      next: (updatedUser) => {
        this.isSubmitting = false;
        this.successMessage = 'Profil mis à jour avec succès.';
        this.profileForm.patchValue({ motDePasse: '' });
      },
      error: (err) => {
        this.isSubmitting = false;
        this.errorMessage = err.error?.message || 'Erreur lors de la mise à jour.';
      }
    });
  }

}
