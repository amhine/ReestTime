import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { UserService } from '../../../core/services/user.service';

@Component({
  selector: 'app-employe-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './employe-form.component.html'
})
export class EmployeFormComponent implements OnInit {
  form: FormGroup;
  isEditMode = false;
  employeId: number | null = null;
  isLoading = false;
  isSaving = false;
  errorMessage = '';

  constructor(
    private fb: FormBuilder,
    private userService: UserService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.form = this.fb.group({
      nom: ['', [Validators.required]],
      prenom: ['', [Validators.required]],
      departement: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      motDePasse: ['', [Validators.required, Validators.minLength(4)]],
      soldeConges: [15, [Validators.required, Validators.min(0)]]
    });
  }

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditMode = true;
      this.employeId = +id;
      this.loadEmploye();
      // Remove password validation for edit mode
      this.form.get('motDePasse')?.clearValidators();
      this.form.get('motDePasse')?.updateValueAndValidity();
    }
  }

  loadEmploye(): void {
    if (!this.employeId) return;

    this.isLoading = true;
    this.userService.getUserById(this.employeId).subscribe({
      next: (user) => {
        this.form.patchValue({
          nom: user.nom,
          prenom: user.prenom,
          departement: user.departement,
          email: user.email,
          soldeConges: user.soldeConges
        });
        this.isLoading = false;
      },
      error: () => {
        this.router.navigate(['/rh/employes']);
      }
    });
  }

  onSubmit(): void {
    if (this.form.invalid) return;

    this.isSaving = true;
    this.errorMessage = '';

    if (this.isEditMode && this.employeId) {
      const updateData = {
        nom: this.form.value.nom,
        prenom: this.form.value.prenom,
        email: this.form.value.email,
        soldeConges: this.form.value.soldeConges,
        departement: this.form.value.departement
      };

      this.userService.updateUser(this.employeId, updateData).subscribe({
        next: () => {
          this.router.navigate(['/rh/employes']);
        },
        error: (err) => {
          this.isSaving = false;
          this.errorMessage = 'Erreur lors de la modification';
        }
      });
    } else {
      const createData = {
        ...this.form.value,
        role: 'EMPLOYE' as const
      };

      this.userService.createUser(createData).subscribe({
        next: () => {
          this.router.navigate(['/rh/employes']);
        },
        error: (err) => {
          this.isSaving = false;
          this.errorMessage = err.error?.message || 'Erreur lors de la création';
        }
      });
    }
  }
}
