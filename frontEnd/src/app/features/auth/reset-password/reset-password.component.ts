import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../../environments/environment';

@Component({
  selector: 'app-reset-password',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './reset-password.component.html'
})
export class ResetPasswordComponent implements OnInit {
  resetForm: FormGroup;
  isLoading = false;
  success = false;
  error = '';
  token = '';

  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.resetForm = this.fb.group({
      nouveauMotDePasse: ['', [Validators.required, Validators.minLength(4)]]
    });
  }

  ngOnInit(): void {
    this.token = this.route.snapshot.queryParamMap.get('token') || '';
    if (!this.token) {
      this.error = "Jeton de réinitialisation manquant ou invalide.";
    }
  }

  onSubmit(): void {
    if (this.resetForm.valid && this.token) {
      this.isLoading = true;
      this.http.post(`${environment.apiUrl}/auth/resetpassword`, {
        token: this.token,
        nouveauMotDePasse: this.resetForm.value.nouveauMotDePasse
      }, { responseType: 'text' }).subscribe({
        next: () => {
          this.success = true;
          this.isLoading = false;
        },
        error: (err) => {
          this.error = "Erreur lors de la réinitialisation. Le lien a peut-être expiré.";
          this.isLoading = false;
        }
      });
    }
  }
}
